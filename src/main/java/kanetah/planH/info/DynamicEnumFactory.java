package kanetah.planH.info;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

/**
 * created by kane on 2017/08/11
 *
 * 动态枚举工厂
 * 在运行时为枚举类添加枚举值
 */
class DynamicEnumFactory {

    private static ReflectionFactory reflectionFactory
            = ReflectionFactory.getReflectionFactory();

    /**
     * 为枚举类添加一个枚举值
     *
     * @param enumType 枚举类的Class对象
     * @param enumName 被添加的枚举名
     * @param additionalTypes 枚举构造器参数类型数组
     * @param additionalValues 枚举构造器参数数组
     * @param <T> 枚举类型
     */
    @SuppressWarnings("unchecked")
    static <T extends Enum<?>> void addEnum(
            Class<T> enumType,
            String enumName,
            Class<?>[] additionalTypes,
            Object[] additionalValues
    ) {
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new RuntimeException(
                    "class " + enumType + " is not an instance of Enum"
            );
        }

        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().contains("$VALUES")) {
                valuesField = field;
                break;
            }
        }
        AccessibleObject.setAccessible(new Field[]{valuesField}, true);

        try {
            assert valuesField != null;
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<>(Arrays.asList(previousValues));

            T newValue = (T) makeEnum(
                    enumType,
                    enumName,
                    values.size(),
                    additionalTypes,
                    additionalValues
            );
            values.add(newValue);
            setFailSafeFieldValue(
                    valuesField, null,
                    values.toArray((T[]) Array.newInstance(enumType, 0)));
            cleanEnumCache(enumType);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void setFailSafeFieldValue(
            Field field, Object target, Object value
    ) throws NoSuchFieldException, IllegalAccessException {

        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);

        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);

        FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
        fa.set(target, value);
    }

    private static void blankField(
            Class<?> enumClass, String fieldName
    ) throws NoSuchFieldException, IllegalAccessException {

        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                AccessibleObject.setAccessible(new Field[]{field}, true);
                setFailSafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }

    private static void cleanEnumCache(
            Class<?> enumClass
    ) throws NoSuchFieldException, IllegalAccessException {
        blankField(enumClass, "enumConstantDirectory");
        blankField(enumClass, "enumConstants");
    }

    private static ConstructorAccessor getConstructorAccessor(
            Class<?> enumClass,
            Class<?>[] additionalParameterTypes
    ) throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
    }

    private static Object makeEnum(
            Class<?> enumClass,
            String value,
            int ordinal,
            Class<?>[] additionalTypes,
            Object[] additionalValues
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException {

        Object[] parms = new Object[additionalValues.length + 2];
        parms[0] = value;
        parms[1] = ordinal;
        System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);

        return enumClass.cast(
                getConstructorAccessor(enumClass, additionalTypes).newInstance(parms)
        );
    }
}
