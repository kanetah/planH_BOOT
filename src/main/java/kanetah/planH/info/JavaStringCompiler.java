package kanetah.planH.info;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;

@Component
public class JavaStringCompiler {

	private JavaCompiler compiler;
	private StandardJavaFileManager stdManager;

	public JavaStringCompiler() {
		this.compiler = ToolProvider.getSystemJavaCompiler();
		this.stdManager = compiler.getStandardFileManager(
		        null, null, null);
	}

	public Map<String, byte[]> compile(String fileName, String source) {
		try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
			JavaFileObject javaFileObject = manager.makeStringSource(fileName, source);
			CompilationTask task =
                    compiler.getTask(null, manager, null, null,
                            null, Collections.singletonList(javaFileObject));
			Boolean result = task.call();
			if (result == null || !result) {
				throw new RuntimeException("Compilation failed.");
			}
			return manager.getClassBytes();
		} catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

	public Class<?> loadClass(String name, Map<String, byte[]> classBytes) {

		try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
			return classLoader.loadClass(name);
		} catch (IOException | ClassNotFoundException e) {
		    throw new RuntimeException(e.getMessage(), e);
        }
    }
}

class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final Map<String, byte[]> classBytes = new HashMap<>();

    MemoryJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    Map<String, byte[]> getClassBytes() {
        return new HashMap<>(this.classBytes);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
        classBytes.clear();
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
            Location location,
            String className,
            JavaFileObject.Kind kind,
            FileObject sibling
    ) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS)
            return new MemoryOutputJavaFileObject(className);
        else
            return super.getJavaFileForOutput(location, className, kind, sibling);
    }

    JavaFileObject makeStringSource(String name, String code) {
        return new MemoryInputJavaFileObject(name, code);
    }

    static class MemoryInputJavaFileObject
            extends SimpleJavaFileObject {

        final String code;

        MemoryInputJavaFileObject(String name, String code) {
            super(URI.create("string:///" + name), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
            return CharBuffer.wrap(code);
        }
    }

    class MemoryOutputJavaFileObject
            extends SimpleJavaFileObject {
        final String name;

        MemoryOutputJavaFileObject(String name) {
            super(URI.create("string:///" + name), Kind.CLASS);
            this.name = name;
        }

        @Override
        public OutputStream openOutputStream() {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
                @Override
                public void close() throws IOException {
                    out.close();
                    ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                    classBytes.put(name, bos.toByteArray());
                }
            };
        }
    }
}

class MemoryClassLoader extends URLClassLoader {

    private Map<String, byte[]> classBytes = new HashMap<>();

    MemoryClassLoader(Map<String, byte[]> classBytes) {
        super(new URL[0], MemoryClassLoader.class.getClassLoader());
        this.classBytes.putAll(classBytes);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz =  Info.forName(name);
        if (clazz != null)
            return clazz;

        byte[] buf = classBytes.get(name);
        if (buf == null)
            return super.findClass(name);
        classBytes.remove(name);
        return defineClass(name, buf, 0, buf.length);
    }
}