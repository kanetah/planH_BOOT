package top.kanetah.planH.pojo;

import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;

public enum SaveFormat {
    code,
    name,
    title,
    subject;

    private static final String defaultFormat = "[title] [code] [name]";

    @Override
    public String toString() {
        return "[" + this.name() + "]";
    }

    public static String format(Task task, User user) {
        String format = task.getSaveFormat();
        format = (format == null || format.equals("")) ? defaultFormat : format;
        format = format.replace(code.toString(), String.valueOf(user.getUserCode()));
        format = format.replace(name.toString(), user.getUserName());
        format = format.replace(title.toString(), task.getTitle());
        format = format.replace(subject.toString(), task.getSubject());
        return format.trim();
    }
}
