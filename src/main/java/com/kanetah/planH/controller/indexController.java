package com.kanetah.planH.controller;

import com.kanetah.planH.service.RoleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class indexController {

    private final RoleViewService roleViewService;

    @Autowired
    public indexController(RoleViewService roleViewService) {
        this.roleViewService = roleViewService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String toIndexView() {
        return roleViewService.getViewByRole();
    }
}
