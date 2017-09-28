package top.kanetah.planH.fileSaveProcessor;

import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;

import java.io.IOException;

public interface SubjectTaskFileSaveProcessor {
    void saveFile(User user, Task task, MultipartFile file) throws IOException;
}
