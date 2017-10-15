package top.kanetah.planH.format;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

@Component
@FormatType("计算机英语定制格式化处理器")
public class ComputerEnglishFormatProcessor implements FormatSaveProcessor {

    private static FormatSaveProcessor processor = null;
    @Value(value = "${kanetah.planH.userPatchFileStorePath}")
    private String storePath;

    public ComputerEnglishFormatProcessor() {
        this.storePath = path;
    }

    @Override
    public String saveFile(User user, Task task, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        if (!Pattern.compile("^G\\d{1,2}-U\\d{1,2}-[BbCc]\\.((doc)|(docx)|(DOC)|(DOCX))$")
                .matcher(originalFilename).matches())
            throw new FileNameException();
        String path = storePath + "/" + task.getSubject() + "/" + task.getTitle();
        File target = new File(path);
        if (!target.exists())
            if (!target.mkdirs())
                throw new FileException();
        target = new File(path + "/" + originalFilename);
        file.transferTo(target);
        return target.getName();
    }

    @Override
    public Boolean fileUserWhenSendMail() {
        return false;
    }

    public static FormatSaveProcessor create() {
        if (processor == null) processor = new ComputerEnglishFormatProcessor();
        return processor;
    }
}
