package kanetah.planH.info;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class Info {

    @SuppressWarnings("unchecked")
    public List<InfoEnumInterface> getEnumList(Class targetClass) {
        try {
            String className = targetClass.getName();
            className += "Attribute";
            Field field = Class.forName(className).
                    getField("All" + className.substring(className.lastIndexOf('.') + 1));
            return (List<InfoEnumInterface>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
