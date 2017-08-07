package com.kanetah.planH.controller;

import com.kanetah.planH.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
public class adminController {

    private final AdminService adminService;

    @Autowired
    public adminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/post"
//            method = RequestMethod.POST
    )
    public Map<String,Object> addTask(
//            @RequestParam(value = "subject") String subject,
//            @RequestParam(value = "content") String content
    ) {
//        adminService.addTask(task);
        System.out.println("poi!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(subject);
//        System.out.println(content);
        System.out.println();
        Map<String, Object> map = new HashMap<>();
        map.put("return", "nico");
        return map;
    }
}
