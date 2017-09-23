package top.kanetah.planH.service;

import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.entity.relationship.Submit;
import top.kanetah.planH.info.Info;
import top.kanetah.planH.info.InfoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.pojo.SaveFormat;
import top.kanetah.planH.tools.CompactTool;
import top.kanetah.planH.tools.FileTool;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    private final RepositoryService repositoryService;
    private final Info info;
    @Value(value = "${kanetah.planH.userPatchFileStorePath}")
    private String storePath;
    private Map<Long, List<Task>> BuffMap = new HashMap<>();

    @Autowired
    public UserService(
            RepositoryService repositoryService,
            InfoImpl info
    ) {
        this.repositoryService = repositoryService;
        this.info = info;
    }

    public String getUserName(long userCode) {
        return
                repositoryService.userRepository
                        .findByUserCode(userCode).getUserName();
    }

    public List<Map<String, Object>> getTask(int from, int to, long userCode) {
        List<Object> taskInfoList = new ArrayList<>();
        Map<Long, Submit> submitMap = new HashMap<>();
        repositoryService.submitRepository
                .findAllByUser_UserCode(userCode).forEach(submit -> {
            Submit oldSubmit = submitMap.get(submit.getTask().getId());
            if (oldSubmit != null
                    && oldSubmit.getSubmitDate().compareTo(submit.getSubmitDate()) > 0)
                return;
            submitMap.put(
                    submit.getTask().getId(),
                    submit
            );
        });

        if (from == 0) {
            Stack<Task> taskStack = new Stack<>();
            List<Task> submitted = new ArrayList<>();
            repositoryService.taskRepository
                    .findAll().iterator()
                    .forEachRemaining(task -> {
                        if (submitMap.get(task.getId()) == null)
                            taskStack.push(task);
                        else
                            submitted.add(task);
                    });
            List<Task> taskList = new ArrayList<>();
            while (!taskStack.empty())
                taskList.add(taskStack.pop());
            taskList.addAll(submitted);
            BuffMap.put(userCode, taskList);
        }

        List<Task> tasks = BuffMap.get(userCode);
        for (int i = from; i < to; ++i)
            try {
                Task task = tasks.get(i);
                Submit submit = submitMap.get(task.getId());
                if (submit == null) submit = Submit.emptySubmit();
                taskInfoList.add(
                        info.byOrigin(
                                task,
                                submit
                        )
                );
            } catch (IndexOutOfBoundsException e) {
                break;
            }

        List<Map<String, Object>> ajaxList = new ArrayList<>();
        Object[] taskInfo = new Object[1];
        taskInfoList.forEach(task -> {
            taskInfo[0] = task;
            HashMap<String, Object> infoMap = new HashMap<>();
            info.getEnumList(task.getClass()).forEach(attribute ->
                    infoMap.put(
                            attribute.getValue(),
                            attribute.invokeMargetMethod(taskInfo[0])
                    ));
            ajaxList.add(infoMap);
        });

        return ajaxList;
    }

    public void submitTask(
            long userCode, Long taskId, MultipartFile file
    ) throws IOException {
        Optional<Task> optional = repositoryService.taskRepository.findById(taskId);
        assert optional.isPresent();
        Task task = optional.get();
        User user = repositoryService.userRepository.findByUserCode(userCode);

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
        path += "/" + SaveFormat.format(task, user);
        target = new File(path + fileType);
        if (!target.exists())
            if (!target.createNewFile())
                throw new FileException();
        file.transferTo(target);

        Submit submit = new Submit(user, task, originalFilename, new Date());
        repositoryService.submitRepository.save(submit);

        String descDir = path + "/";
        switch (fileType) {
            case ".zip":
                CompactTool.unZip(target, descDir);
                afterDecompression(path);
                if (!target.delete())
                    throw new FileException();
                break;
            case ".rar":
                CompactTool.unRar(target, descDir);
                afterDecompression(path);
                if (!target.delete())
                    throw new FileException();
                break;
        }
    }

    private void afterDecompression(String descDir) {
        File[] files = new File(descDir).listFiles();
        if (files == null)
            throw new CompactFileException();
        else if (files.length == 1 && files[0].isDirectory()) {
            File dir = files[0];
            files = dir.listFiles();
            if (files == null)
                throw new CompactFileException();
            for (File f : files)
                if (!f.renameTo(new File(descDir + "/" + f.getName()))
                        && !FileTool.deleteAll(f))
                    throw new FileException();
            if (!dir.delete())
                throw new FileException();
        }
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "服务端无法处理文件")
    private class FileException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "文件类型错误")
    private class FileTypeException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "无效的压缩文件")
    private class CompactFileException extends RuntimeException {
    }
}
