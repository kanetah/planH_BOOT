package top.kanetah.planH.info;

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

    // 反射工厂
    private static ReflectionFactory reflectionFactory
            = ReflectionFactory.getReflectionFactory();

    /**
     * 为枚举类添加一个枚举
     * 基于反射，把枚举类的属性列表全部取出来，增加一个新的枚举以后再放回去
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
        // 类型检查
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new RuntimeException(
                    "class " + enumType + " is not an instance of Enum"
            );
        }

        // 寻找枚举类中的"$VALUES"域，取消其默认java语言访问控制检查
        Field valuesField = null;
        for (Field field : enumType.getDeclaredFields())
            if (field.getName().contains("$VALUES")) {
                valuesField = field;
                break;
            }
        AccessibleObject.setAccessible(new Field[]{valuesField}, true);

        try {
            assert valuesField != null;
            // 复制原枚举
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<>(Arrays.asList(previousValues));

            // 构造新的枚举
            T newValue = (T) makeEnum(
                    enumType,
                    enumName,
                    values.size(),
                    additionalTypes,
                    additionalValues
            );
            // 添加新枚举
            values.add(newValue);
            // 重新设置枚举类的域
            setFailSafeFieldValue(
                    valuesField, null,
                    values.toArray((T[]) Array.newInstance(enumType, 0)));
            // 清空枚举缓存
            cleanEnumCache(enumType);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 无视属性修饰符向域中强制写入值
     *
     * @param field 被修改的域
     * @param target 被修改的对象
     * @param value 值
     * @throws NoSuchFieldException 类中没有指定的域
     * @throws IllegalAccessException 非法存取
     */
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

    /**
     * 强制修改一个指定枚举的指定域为null
     *
     * @param enumClass 被修改的枚举
     * @param fieldName 要修改的域名
     * @throws NoSuchFieldException 类中没有指定的域
     * @throws IllegalAccessException 非法存取
     */
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

    /**
     * 清除枚举缓存
     *
     * @param enumClass 枚举类
     * @throws NoSuchFieldException 类中没有指定的域
     * @throws IllegalAccessException 非法存取
     */
    private static void cleanEnumCache(
            Class<?> enumClass
    ) throws NoSuchFieldException, IllegalAccessException {
        blankField(enumClass, "enumConstantDirectory");
        blankField(enumClass, "enumConstants");
    }

    /**
     * 获取枚举构造器接口
     *
     * @param enumClass 被获取的枚举
     * @param additionalParameterTypes 构造器参数类型数组
     * @return 构造器接口
     * @throws NoSuchMethodException 类中没有指定的域
     */
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

    /**
     * 生成一个新的枚举值
     *
     * @param enumClass 枚举类Class对象
     * @param value 新枚举值的名字
     * @param ordinal 序数
     * @param additionalTypes 构造器参数类型数组
     * @param additionalValues 构造器参数数组
     * @return 新的枚举值
     * @throws NoSuchMethodException 类中没有指定的域
     * @throws InvocationTargetException 调用目标异常
     * @throws InstantiationException 实例化异常
     */
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
