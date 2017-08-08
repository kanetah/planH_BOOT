package com.kanetah.planH.controller;

import com.kanetah.planH.entity.node.Task;
import com.kanetah.planH.entity.node.User;
import com.kanetah.planH.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
public class adminController {

    private final AdminService adminService;
    private Map<String, Object> map = new HashMap<>();

    {
        map.put("status", "created");
    }

    @Autowired
    public adminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/create",
            method = RequestMethod.POST
    )
    public Map<String, Object> createTask(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "date") String date
    ) {
        adminService.createTask(new Task(subject, content, date));
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
}
