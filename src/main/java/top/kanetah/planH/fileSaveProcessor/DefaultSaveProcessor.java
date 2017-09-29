package top.kanetah.planH.fileSaveProcessor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.pojo.SaveFormat;
import top.kanetah.planH.tools.CompactTool;
import top.kanetah.planH.tools.FileTool;

import java.io.File;
import java.io.IOException;

@Component
@SupportSaveType("最外层文件名格式化存储处理器")
public class DefaultSaveProcessor implements SubjectTaskFileSaveProcessor {

    @Value(value = "${kanetah.planH.userPatchFileStorePath}")
    private String storePath;

    @Override
    public void saveFile(
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
        path += "/" + SaveFormat.format(task, user, file);
        target = new File(path + fileType);
        if (!target.exists())
            if (!target.createNewFile())
                throw new FileException();
        file.transferTo(target);
        handleCompactFile(target, path, fileType);
    }

    private void handleCompactFile(File target, String path, String fileType){
        switch (fileType) {
            case ".zip":
                CompactTool.unZip(target, path + "/");
                break;
            case ".rar":
                CompactTool.unRar(target, path + "/");
                break;
        }
        File[] files = new File(path).listFiles();
        if (files == null)
            throw new CompactFileException();
        else if (files.length == 1 && files[0].isDirectory()) {
            File dir = files[0];
            files = dir.listFiles();
            if (files == null)
                throw new CompactFileException();
            for (File f : files)
                if (!f.renameTo(new File(path + "/" + f.getName()))
                        && !FileTool.deleteAll(f))
                    throw new FileException();
            if (!dir.delete())
                throw new FileException();
        }
        if (!target.delete())
            throw new FileException();
    }
}
