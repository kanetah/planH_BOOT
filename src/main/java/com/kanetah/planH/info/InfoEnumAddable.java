package com.kanetah.planH.info;

import static com.kanetah.planH.info.DynamicEnumFactory.*;

interface InfoEnumAddable {
    static <T extends Enum<?>> void addInfoEnum(
            Class<T> enumType,
            String value
    ) {
        addEnum(
                enumType,
                value,
                new Class[]{String.class},
                new Object[]{value}
        );
    }
}
