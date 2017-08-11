package kanetah.planH.info;

interface InfoEnumAddable {
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
