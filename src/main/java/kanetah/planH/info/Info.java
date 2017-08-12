package kanetah.planH.info;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class Info implements InitializingBean {

    @Value(value = "${kanetah.planH.infoEntityJsonPath}")
    private String infoEntityJsonPath;
    @Value(value = "${kanetah.planH.infoAttributeTemplatePath}")
    private String infoAttributeTemplatePath;
    private final JavaStringCompiler compiler;
    private Map<String, Class<?>> classMap = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public Info(JavaStringCompiler compiler) {
        this.compiler = compiler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Resource resource = new ClassPathResource(infoEntityJsonPath);
        File infoEntityJsonFile = resource.getFile();
        Map jsonData =
                mapper.readValue(infoEntityJsonFile, Map.class);
        jsonData.forEach((className,value) -> {
            if(value instanceof Map)
                ((Map) value).forEach((sk, sv) -> {
                    if(sv instanceof Iterable)
                        ((Iterable) sv).forEach(
                                System.out::println
                        );
                });
        });

        // fixme
        List<String> classNameList = Collections.singletonList("TaskInfo");

        resource = new ClassPathResource(infoAttributeTemplatePath);
        File infoAttributeTemplateFile = resource.getFile();
        Reader reader = new FileReader(infoAttributeTemplateFile);
        String contentString = FileCopyUtils.copyToString(reader);

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
