package top.kanetah.planH.format;

import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;

import java.io.IOException;

public interface FormatSaveProcessor {
    String path = "/planHFile"; // FIXME: 2017/10/15
    String saveFile(User user, Task task, MultipartFile file) throws IOException;
    Boolean fileUserWhenSendMail();
}
