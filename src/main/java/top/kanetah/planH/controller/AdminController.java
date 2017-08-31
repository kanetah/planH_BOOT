package top.kanetah.planH.controller;

import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final Map<String, Object> map = new HashMap<>();

    {
        map.put("status", "created");
    }

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/create",
            method = RequestMethod.POST
    )
    public Map<String, Object> createTask(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "deadline") String deadline
    ) {
        adminService.createTask(new Task(subject, title, content, deadline));
        return map;
    }

    @ResponseBody
    @RequestMapping(
            value = "/user/create",
            method = RequestMethod.POST
    )
    public Map<String, Object> createUser(
            @RequestParam(value = "code") long code,
            @RequestParam(value = "name") String name
    ) {
        adminService.createUser(new User(code, name));
        return map;
    }

    @ResponseBody
    @RequestMapping(
            value = "/user/batch",
            method = RequestMethod.POST
    )
    public Map<String, Object> batchCreateUser(
            @RequestPart(value = "file") MultipartFile file
    ) throws IOException {
        adminService.batchCreateUser(file);
        return map;
    }

    @ResponseBody
    @RequestMapping(
            value = "/reset",
            method = RequestMethod.GET
    )
    public Map<String, Object> resetAdmin(
    ) {
        adminService.resetAdmin();
        return map;
    }
}
