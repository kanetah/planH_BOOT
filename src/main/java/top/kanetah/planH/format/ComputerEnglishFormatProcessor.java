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
        super();
        processor = this;
    }

    @Override
    public String saveFile(User user, Task task, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        if(!Pattern.compile("^G\\d{1,2}-U\\d{1,2}-[BbCc]\\.((zip)|(rar)|(ZIP)|(RAR))$")
                .matcher(originalFilename).matches())
            throw new FileNameException();
        String fileType = originalFilename.substring(originalFilename.indexOf("."));
        originalFilename = originalFilename.substring(0, originalFilename.indexOf("."));
        String path = storePath + "/" + task.getSubject() + "/" + task.getTitle();
        File target = new File(path);
        if (!target.exists())
            if (!target.mkdirs())
                throw new FileException();
        path += "/" + originalFilename;
        File compactTarget = new File(path + fileType);
        if (!compactTarget.exists())
            if (!compactTarget.createNewFile())
                throw new FileException();
        file.transferTo(compactTarget);
        CompactFileProcessor.handleCompactFile(compactTarget, path, fileType);
        File[] contents = new File(path).listFiles();
        assert contents != null;
        File summary = null;
        for (File c : contents) {
            String name = c.getName();
            if (name.contains(".") && name.substring(name.lastIndexOf('.')).startsWith(".doc"))
                if (summary == null)
                    summary = c;
                else
                    throw new DirectoryContentException();
        }
        if (summary == null)
            throw new DirectoryContentException();
        String summaryName = summary.getName();
        String summaryType = summaryName.substring(summaryName.indexOf('.'));
        if (!summary.renameTo(new File(path + "/" + originalFilename + summaryType)))
            throw new FileException();
        return target.getName();
    }

    @Override
    public Boolean fileUserWhenSendMail() {
        return false;
    }

    public static FormatSaveProcessor create() {
//        if(processor == null)
//            processor = new ComputerEnglishFormatProcessor();
        return processor;
    }
}
