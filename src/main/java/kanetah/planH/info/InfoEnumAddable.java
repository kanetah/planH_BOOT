package kanetah.planH.info;

/**
 * created by kane on 2017/08/11
 *
 * 可添加新枚举
 */
public interface InfoEnumAddable {

    /**
     * 为一个枚举类添加新枚举
     *
     * @param enumType 枚举类Class对象
     * @param value 枚举名
     * @param <T> 枚举类
     */
    static <T extends Enum<?>> void addInfoEnum(
            Class<T> enumType,
            String value
    ) {
        DynamicEnumFactory.addEnum(
                enumType,
                value,
                new Class[]{String.class},
                new Object[]{value}
        );
    }
}
