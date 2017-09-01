package top.kanetah.planH.info;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * created by kane on 2017/08/11
 * <p>
 * 向前端发送的信息由此类进行处理
 */
@Component
@SuppressWarnings("unchecked")
public class InfoImpl implements Info, InitializingBean {

    // 通过这些属性影响动态生成时的行为
    @Value(value = "${kanetah.planH.infoEntityJsonPath}")
    private Resource infoEntityJsonResource;
    @Value(value = "${kanetah.planH.InfoEntityFrameTemplate}")
    private Resource infoEntityFrameTemplateResource;
    @Value(value = "${kanetah.planH.InfoEntityFieldTemplate}")
    private Resource infoEntityFieldTemplateResource;
    @Value(value = "${kanetah.planH.InfoEntityMethodTemplate}")
    private Resource infoEntityMethodTemplateResource;
    @Value(value = "${kanetah.planH.infoAttributeTemplatePath}")
    private Resource infoAttributeTemplateResource;
    @Value(value = "${kanetah.planH.infoClassPackageName}")
    private String infoClassPackageName;
    @Value(value = "${kanetah.planH.infoClassEnumSuffix}")
    private String infoClassEnumSuffix;

    // 字符串字节码编译器
    private final JavaStringCompiler compiler;

    @Autowired
    public InfoImpl(JavaStringCompiler compiler) {
        this.compiler = compiler;
    }

    /**
     * Spring Bean 初始化方法
     * 初始化时，将会读取配置好的资源文件并基于此生成指定的类
     *
     * @throws Exception 初始化时的异常，将被Spring捕捉并作为Runtime异常抛出
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        // 指定报表类内容的配置是一个json文件，通过jackson处理
        Map jsonData =
                new ObjectMapper().readValue(infoEntityJsonResource.getFile(), Map.class);

        String frameString = FileCopyUtils.copyToString(
                new FileReader(infoEntityFrameTemplateResource.getFile()));
        String fieldString = FileCopyUtils.copyToString(
                new FileReader(infoEntityFieldTemplateResource.getFile()));
        String methodString = FileCopyUtils.copyToString(
                new FileReader(infoEntityMethodTemplateResource.getFile()));
        final String[] targetClassName = {""};

        /* 动态加载配置的报表类 */
        jsonData.forEach((className, value) -> {

            /* 开始处理模板并生成字符串 */
            targetClassName[0] = String.valueOf(className);
            final String[] codeString = {""};

            infoClassNameList.add(targetClassName[0]);
            codeString[0] = frameString.replace("\"\"\"className\"\"\"", targetClassName[0]);
            final String[] initString = {""};
            final String[] argsNameValue = {""};
            codeString[0] = codeString[0].replace("\"\"\"packageName\"\"\"", infoClassPackageName);
            final String[] tokenString = {""};

            if (value instanceof Map)
                ((Map) value).forEach((originClassName, originClassField) -> {
                    tokenString[0] += originClassName + " ";
                    String originClassNameString = String.valueOf(originClassName);
                    final String[] argsNameValue_Value = {""};
                    argsNameValue_Value[0] = originClassNameString.substring(
                            originClassNameString.lastIndexOf('.') + 1);
                    argsNameValue_Value[0] = argsNameValue_Value[0].substring(0, 1).toLowerCase() +
                            argsNameValue_Value[0].substring(1);
                    argsNameValue[0] +=
                            originClassNameString + " " +
                                    argsNameValue_Value[0] + ", ";

                    if (originClassField instanceof Iterable) {
                        ((Iterable) originClassField).forEach(fieldName -> {
                                    try {
                                        Class clazz = Class.forName(originClassNameString);
                                        Field field = getDeclaredField(clazz, fieldName.toString());
                                        assert field != null;
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
                    }
                });

            argsNameValue[0] = argsNameValue[0].substring(0, argsNameValue[0].length() - 2);
            codeString[0] = codeString[0].replace(
                    "\"\"\"argsNameValue\"\"\"", argsNameValue[0]
            );
            codeString[0] = codeString[0].replace(
                    "\"\"\"argsInit\"\"\"", initString[0]
            );
            codeString[0] = codeString[0].replaceAll(
                    "\"\"\"InfoEntityFieldTemplate\"\"\"|\"\"\"InfoEntityMethodTemplate\"\"\"",
                    "");
            /* 字符串已生成 */

            String finalTargetClassName = infoClassPackageName + "." + targetClassName[0];
            // 将字符串编译为字节码
            Map<String, byte[]> results = compiler.compile(
                    targetClassName[0] + ".java", codeString[0]);
            // 将字节码加载至java虚拟机
            Class<?> clazz = compiler.loadClass(
                    finalTargetClassName, results);
            classMap.put(finalTargetClassName, clazz);
            classTokenMap.put(tokenString[0], finalTargetClassName);
        });
        /* 报表类完成加载 */

        /* 开始加载枚举类 */
        Reader reader = new FileReader(infoAttributeTemplateResource.getFile());
        String contentString = FileCopyUtils.copyToString(reader);

        infoClassNameList.forEach(name -> {
            String classString = contentString.replace(
                    "\"\"\"OriginClassName\"\"\"", name);
            Map<String, byte[]> results = compiler.compile(
                    name + infoClassEnumSuffix + ".java", classString);
            Class<?> clazz = compiler.loadClass(
                    infoClassPackageName + "." + name + infoClassEnumSuffix, results);
            classMap.put(infoClassPackageName + "." + name + infoClassEnumSuffix, clazz);
        });
        /* 枚举类完成加载 */
    }

    /**
     * 通过源实体对象获取报表对象
     * 使用反射通过已生成的报表类的create方法生成对象
     *
     * @param args 源实体对象
     * @return 报表对象
     */
    @Override
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
                throw new ClassNotFoundException("DynamicClassNotFound");
            Method method = clazz.getMethod("create", argsClass);
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取前端信息实体类属性枚举并通过枚举接口操作前端信息实体对象
     *
     * @param targetClass 前端信息实体类Class对象
     * @return 属性枚举列表
     */
    @Override
    public List<InfoEnumInterface> getEnumList(Class targetClass) {
        try {
            String className = targetClass.getName();
            className += infoClassEnumSuffix;
            Field field = Info.forName(className).
                    getField("All" + className.substring(className.lastIndexOf('.') + 1));
            return (List<InfoEnumInterface>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 遍历继承树来查找一个属性
     *
     * @param clazz     被查找属性的类
     * @param fieldName 属性名
     * @return 查找到的域
     */
    private static Field getDeclaredField(Class<?> clazz, String fieldName) {
        Field field = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (Exception ignored) {
                // 必须捕捉并忽略异常，否则查找无法遍历继承树
            }
        }
        return field;
    }
}
