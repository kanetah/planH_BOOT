package com.kanetah.planH.controller;

import com.kanetah.planH.entity.node.Task;
import com.kanetah.planH.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin")
public class adminController {

    private final AdminService adminService;

    @Autowired
    public adminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(value = "/addTask")
    public void addTask() {
//        adminService.addTask(task);
        System.out.println("poi");
    }
}
