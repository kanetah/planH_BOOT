package kanetah.planH.info;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Component
@SuppressWarnings("unchecked")
public class Info implements InitializingBean {

    @Value(value = "${kanetah.planH.infoEntityJsonPath}")
    private String infoEntityJsonPath;
    @Value(value = "${kanetah.planH.InfoEntityFrameTemplate}")
    private String infoEntityFrameTemplatePath;
    @Value(value = "${kanetah.planH.InfoEntityFieldTemplate}")
    private String infoEntityFieldTemplatePath;
    @Value(value = "${kanetah.planH.InfoEntityMethodTemplate}")
    private String infoEntityMethodTemplatePath;
    @Value(value = "${kanetah.planH.infoAttributeTemplatePath}")
    private String infoAttributeTemplatePath;
    private final JavaStringCompiler compiler;
    private static Map<String, Class<?>> classMap = new HashMap<>();
    private Map<String, String> classTokenMap = new HashMap<>();
    private List<String> infoClassNameList = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();

    private String packageName = "kanetah.planH.info";
    private String enumSuffix = "Attribute";

    @Autowired
    public Info(JavaStringCompiler compiler) {
        this.compiler = compiler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        File infoEntityJsonFile =
                new ClassPathResource(infoEntityJsonPath).getFile();
        File infoEntityFrameTemplateFile =
                new ClassPathResource(infoEntityFrameTemplatePath).getFile();
        File infoEntityFieldTemplateFile =
                new ClassPathResource(infoEntityFieldTemplatePath).getFile();
        File infoEntityMethodTemplateFile =
                new ClassPathResource(infoEntityMethodTemplatePath).getFile();
        Map jsonData =
                mapper.readValue(infoEntityJsonFile, Map.class);

        String frameString = FileCopyUtils.copyToString(
                new FileReader(infoEntityFrameTemplateFile));
        String fieldString = FileCopyUtils.copyToString(
                new FileReader(infoEntityFieldTemplateFile));
        String methodString = FileCopyUtils.copyToString(
                new FileReader(infoEntityMethodTemplateFile));
        String[] targetClassName = new String[1];

        jsonData.forEach((className, value) -> {

            targetClassName[0] = String.valueOf(className);
            final String[] codeString = new String[1];

            infoClassNameList.add(targetClassName[0]);
            codeString[0] = frameString.replace("\"\"\"className\"\"\"", targetClassName[0]);
            String[] initString = {""};
            final String[] argsNameValue = {""};
            codeString[0] = codeString[0].replace("\"\"\"packageName\"\"\"", packageName);
            String[] tokenString = {""};

            if (value instanceof Map)
                ((Map) value).forEach((originClassName, originClassField) -> {
                    tokenString[0] += originClassName + " ";
                    String originClassNameString = String.valueOf(originClassName);
                    String[] argsNameValue_Value = {""};
                    argsNameValue_Value[0] = originClassNameString.substring(
                            originClassNameString.lastIndexOf('.') + 1);
                    argsNameValue_Value[0] = argsNameValue_Value[0].substring(0, 1).toLowerCase() +
                            argsNameValue_Value[0].substring(1);
                    argsNameValue[0] +=
                            originClassNameString + " " +
                                    argsNameValue_Value[0] + ", ";

                    if (originClassField instanceof Iterable)
                        ((Iterable) originClassField).forEach(fieldName -> {
                                    try {
                                        Class clazz = Class.forName(originClassNameString);
                                        Field field = clazz.getDeclaredField(fieldName.toString());
                                        String fieldTypeNameString = field.getType().getTypeName();
                                        String fieldNameString = field.getName();

                                        String buffField = fieldString.replace(
                                                "\"\"\"fieldClassName\"\"\"",
                                                fieldTypeNameString
                                        );
                                        buffField = buffField.replace(
                                                "\"\"\"fieldName\"\"\"",
                                                fieldNameString
                                        );
                                        codeString[0] = codeString[0].replace(
                                                "\"\"\"InfoEntityFieldTemplate\"\"\"",
                                                buffField + "\"\"\"InfoEntityFieldTemplate\"\"\""
                                        );

                                        initString[0] += "        instance." + fieldNameString + " = "
                                                + argsNameValue_Value[0] + ".get"
                                                + fieldNameString.substring(0, 1).toUpperCase() + fieldNameString.substring(1)
                                                + "();\n";

                                        String buffMethod = methodString.replace(
                                                "\"\"\"fieldClassName\"\"\"",
                                                fieldTypeNameString
                                        );
                                        buffMethod = buffMethod.replace(
                                                "\"\"\"fieldName\"\"\"",
                                                fieldNameString
                                        );
                                        buffMethod = buffMethod.replace(
                                                "\"\"\"FieldName\"\"\"",
                                                fieldNameString.substring(0, 1).toUpperCase() + fieldNameString.substring(1)
                                        );
                                        codeString[0] = codeString[0].replace(
                                                "\"\"\"InfoEntityMethodTemplate\"\"\"",
                                                buffMethod + "\"\"\"InfoEntityMethodTemplate\"\"\""
                                        );
                                    } catch (Exception e) {
                                        throw new RuntimeException(e.getMessage(), e);
                                    }
                                }
                        );
                });

            argsNameValue[0] = argsNameValue[0].substring(0, argsNameValue[0].length() - 2);
            codeString[0] = codeString[0].replace(
                    "\"\"\"argsNameValue\"\"\"", argsNameValue[0]
            );
            codeString[0] = codeString[0].replace(
                    "\"\"\"argsInit\"\"\"", initString[0]
            );
            codeString[0] = codeString[0].replaceAll(
                    "\"\"\"InfoEntityFieldTemplate\"\"\"|\"\"\"InfoEntityMethodTemplate\"\"\""
                    , "");

            String finalTargetClassName = "kanetah.planH.info." + targetClassName[0];
            Map<String, byte[]> results = compiler.compile(
                    targetClassName[0] + ".java", codeString[0]);
            Class<?> clazz = compiler.loadClass(
                    finalTargetClassName, results);
            classMap.put(finalTargetClassName, clazz);
            classTokenMap.put(tokenString[0], finalTargetClassName);
        });

        File infoAttributeTemplateFile =
                new ClassPathResource(infoAttributeTemplatePath).getFile();
        Reader reader = new FileReader(infoAttributeTemplateFile);
        String contentString = FileCopyUtils.copyToString(reader);

        infoClassNameList.forEach(name -> {
            String classString = contentString.replace(
                    "\"\"\"OriginClassName\"\"\"", name);
            Map<String, byte[]> results = compiler.compile(
                    name + enumSuffix + ".java", classString);
            Class<?> clazz = compiler.loadClass(
                    packageName + "." + name + enumSuffix, results);
            classMap.put(packageName + "." + name + enumSuffix, clazz);
        });
    }

    public Object byOrigin(Object... args) {
        try {
            StringBuilder token = new StringBuilder();
            for (Object o : args)
                token.append(o.getClass().getName()).append(" ");
            String tokenString = token.toString();
            String[] argsClassName = tokenString.split(" ");
            Class<?>[] argsClass = new Class[argsClassName.length];
            for (int i = 0; i < argsClass.length; ++i)
                argsClass[i] = Class.forName(argsClassName[i]);
            Class clazz = classMap.get(classTokenMap.get(tokenString));
            if (clazz == null)
                throw new RuntimeException("ClassNotFound");
            Method method = clazz.getMethod("create", argsClass);
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<InfoEnumInterface> getEnumList(Class targetClass) {
        try {
            String className = targetClass.getName();
            className += enumSuffix;
            Field field = forName(className).
                    getField("All" + className.substring(className.lastIndexOf('.') + 1));
            return (List<InfoEnumInterface>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Class<?> forName(String name) {
        return classMap.get(name);
    }
}
