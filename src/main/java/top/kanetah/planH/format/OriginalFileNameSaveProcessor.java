package top.kanetah.planH.format;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;

import java.io.File;
import java.io.IOException;

@Component
@FormatType("最外层原文件名存储处理器")
public class OriginalFileNameSaveProcessor implements FormatSaveProcessor {

    private static OriginalFileNameSaveProcessor processor;
    @Value(value = "${kanetah.planH.userPatchFileStorePath}")
    private String storePath;

    public OriginalFileNameSaveProcessor() {
        this.storePath = path;
    }

    @Override
    public String saveFile(
            User user, Task task, MultipartFile file
    ) throws IOException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileType = originalFilename.substring(originalFilename.indexOf("."));
        if (!task.getFileFormat().contains(fileType))
            throw new FileTypeException();
        String path = storePath + "/" + task.getSubject() + "/" + task.getTitle();
        File target = new File(path);
        if (!target.exists())
            if (!target.mkdirs())
                throw new FileException();
        target = new File(path + originalFilename);
        if (!target.exists())
            if (!target.createNewFile())
                throw new FileException();
        file.transferTo(target);
        try {
            CompactFileProcessor.handleCompactFile(target, path, fileType);
        } catch (FileTypeException ignored) {
        }
        return target.getName();
    }

    @Override
    public Boolean fileUserWhenSendMail() {
        return false;
    }

    public static FormatSaveProcessor create() {
        if (processor == null) processor = new OriginalFileNameSaveProcessor();
        return processor;
    }
}
