package kanetah.planH.controller;

import kanetah.planH.entity.node.Task;
import kanetah.planH.entity.node.User;
import kanetah.planH.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> map = new HashMap<>();
        map.put("status", "created");
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
        Map<String, Object> map = new HashMap<>();
        map.put("status", "created");
        return map;
    }
}
