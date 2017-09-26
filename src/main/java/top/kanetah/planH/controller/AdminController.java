package top.kanetah.planH.controller;

import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/get",
            method = RequestMethod.POST
    )
    public Object[] getAllTask() {
        return adminService.getAllTask().toArray();
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/submit",
            method = RequestMethod.POST
    )
    public Object[] getSubmitInfo(
            @RequestParam(value = "taskId") String taskId
    ) {
        return adminService.getSubmitFileInfos(Long.valueOf(taskId)).toArray();
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/create",
            method = RequestMethod.POST
    )
    public String createTask(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "format") String format,
            @RequestParam(value = "deadline") String deadline
    ) {
        adminService.createTask(new Task(subject, title, content, type, format, deadline));
        return "[\"created\"]";
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/update",
            method = RequestMethod.POST
    )
    public String updateTask(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "format") String format,
            @RequestParam(value = "deadline") String deadline
    ) {
        if (deadline.contains("T"))
            deadline = deadline.substring(0, deadline.indexOf("T"));
        adminService.updateTask(
                Long.valueOf(id),
                subject,
                title,
                content,
                type,
                format,
                deadline
        );
        return "[\"updated\"]";
    }

    @ResponseBody
    @RequestMapping(
            value = "/user/create",
            method = RequestMethod.POST
    )
    public String createUser(
            @RequestParam(value = "code") long code,
            @RequestParam(value = "name") String name
    ) {
        adminService.createUser(new User(code, name));
        return "[\"created\"]";
    }

    @ResponseBody
    @RequestMapping(
            value = "/user/batch",
            method = RequestMethod.POST
    )
    public String batchCreateUser(
            @RequestPart(value = "file") MultipartFile file
    ) throws IOException {
        adminService.batchCreateUser(file);
        return "[\"succeed\"]";
    }

    @ResponseBody
    @RequestMapping(
            value = "/reset",
            method = RequestMethod.GET
    )
    public String resetAdmin() {
        adminService.resetAdmin();
        return "[\"succeed\"]";
    }
}
