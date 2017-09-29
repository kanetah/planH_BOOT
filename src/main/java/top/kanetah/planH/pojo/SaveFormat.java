package top.kanetah.planH.pojo;

import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;

public enum SaveFormat {
    code,
    code2,
    name,
    title,
    subject,
    file;

    public static final String defaultFormat = "[title] [code] [name]";

    @Override
    public String toString() {
        return "[" + this.name() + "]";
    }

    public static String format(Task task, User user, MultipartFile file) {
        String format = task.getSaveFormat();
        format = (format == null || format.equals("")) ? defaultFormat : format;
        format = format.replace(code.toString(), String.valueOf(user.getUserCode()));
        format = format.replace(code2.toString(), String.valueOf(user.getUserCode()).substring(1));
        format = format.replace(name.toString(), user.getUserName());
        format = format.replace(title.toString(), task.getTitle());
        format = format.replace(subject.toString(), task.getSubject());
        assert file.getOriginalFilename() != null;
        format = format.replace(file.toString(), file.getOriginalFilename());
        return format.trim();
    }
}
