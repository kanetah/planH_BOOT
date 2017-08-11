package kanetah.planH.info;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class Info implements InitializingBean {

    @Value(value = "${kanetah.planH.infoAttributeTemplatePath}")
    private String enumFilePath;
    private final JavaStringCompiler compiler;
    private Map<String, Class<?>> classMap = new HashMap<>();

    @Autowired
    public Info(JavaStringCompiler compiler) {
        this.compiler = compiler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Resource resource = new ClassPathResource(enumFilePath);
        File infoAttributeTemplateFile = resource.getFile();
        FileReader reader = new FileReader(infoAttributeTemplateFile);
        String contentString = FileCopyUtils.copyToString(reader);

        // fixme
        List<String> classNameList = Collections.singletonList("TaskInfo");

        classNameList.forEach(name -> {
            String classString = contentString.replace(
                    "\"\"\"OriginClassName\"\"\"", name);
            Map<String, byte[]> results = compiler.compile(
                     name + "Attribute.java", classString);
            Class<?> clazz = compiler.loadClass(
                    "kanetah.planH.info." + name + "Attribute", results);
            classMap.put("kanetah.planH.info." + name + "Attribute", clazz);
        });
    }

    @SuppressWarnings("unchecked")
    public List<InfoEnumInterface> getEnumList(Class targetClass) {
        try {
            String className = targetClass.getName();
            className += "Attribute";
            Field field = forName(className).
                    getField("All" + className.substring(className.lastIndexOf('.') + 1));
            return (List<InfoEnumInterface>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Class<?> forName(String name) {
        return classMap.get(name);
    }
}
