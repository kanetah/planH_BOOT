package kanetah.planH.info;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TaskInfoAttribute {
    ;

    public static final List<TaskInfoAttribute> AllTaskInfoAttribute = new ArrayList<>();

    static {
        for (Field field : TaskInfo.class.getDeclaredFields())
            InfoEnumAddable.addInfoEnum(TaskInfoAttribute.class, field.getName());

        AllTaskInfoAttribute.addAll(Arrays.asList(TaskInfoAttribute.values()));
    }

    private final String value;
    private final String method;

    TaskInfoAttribute(String value) {
        this.value = value;
        this.method = "get" + value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    public Object invokeMargetMethod(Object object) {
        try {
            Method method = object.getClass().getMethod(getMethod());
            return method.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String getValue() {
        return value;
    }

    public String getMethod() {
        return method;
    }
}
