package kanetah.planH.info;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TaskInfoAttribute implements InfoEnumInterface {
    ;

    public static final List<InfoEnumInterface> AllTaskInfoAttribute = new ArrayList<>();
    private static final Class targetClass;

    static {
        try {
            String className = new Object() {
                String getClassName() {
                    String subClassName = this.getClass().getName();
                    return subClassName.substring(0, subClassName.lastIndexOf('$'));
                }
            }.getClassName();
            String targetClassName = className.replace("Attribute", "");
            targetClass = Class.forName(targetClassName);

            for (Field field : targetClass.getDeclaredFields())
                InfoEnumAddable.addInfoEnum(TaskInfoAttribute.class, field.getName());

            AllTaskInfoAttribute.addAll(
                    Arrays.asList(
                            TaskInfoAttribute.values()
                    ));

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private final String value;
    private final String method;

    TaskInfoAttribute(String value) {
        this.value = value;
        this.method = "get" + value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    @Override
    public Object invokeMargetMethod(Object object) {
        try {
            assert object.getClass().isAssignableFrom(targetClass);
            Method method = object.getClass().getMethod(getMethod());
            return method.invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    public String getMethod() {
        return method;
    }
}
