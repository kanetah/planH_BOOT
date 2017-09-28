package top.kanetah.planH.tools;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InterfaceTool {

    @SuppressWarnings("unchecked")
    public static List<Class> getAllClassByInterface(Class c) {
        List<Class> returnClassList = null;
        if (c.isInterface()) {
            String packageName = c.getPackage().getName();
            List<Class<?>> allClass = getClasses(packageName);
            if (allClass != null) {
                returnClassList = new ArrayList<>();
                for (Class classes : allClass)
                    if (c.isAssignableFrom(classes))
                        if (!c.equals(classes))
                            returnClassList.add(classes);
            }
        }
        return returnClassList;
    }

    private static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, true, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.charAt(0) == '/')
                            name = name.substring(1);
                        if (name.startsWith(packageDirName)) {
                            int idx = name.lastIndexOf('/');
                            if (idx != -1)
                                packageName = name.substring(0, idx).replace('/', '.');
                            if (name.endsWith(".class") && !entry.isDirectory())
                                classes.add(Class.forName(packageName + '.' + name.substring(packageName.length() + 1, name.length() - 6)));
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    private static void findAndAddClassesInPackageByFile(
            String packageName, String packagePath, final boolean recursive, List<Class<?>> classes
    ) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory())
            return;

        File[] dirFiles = dir.listFiles(file ->
                (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));
        assert dirFiles != null;
        for (File file : dirFiles)
            if (file.isDirectory())
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classes);
            else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
    }
}
