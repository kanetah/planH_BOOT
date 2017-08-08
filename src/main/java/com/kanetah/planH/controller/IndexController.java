package com.kanetah.planH.controller;

import com.kanetah.planH.service.RoleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    private final RoleViewService roleViewService;

    @Autowired
    public IndexController(RoleViewService roleViewService) {
        this.roleViewService = roleViewService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String toIndexView() {
        return roleViewService.getViewByRole();
    }

    @ResponseBody
    @RequestMapping(value = "role/get")
    public Map<String, Object> get() {

        Map<String, Object> map = new HashMap<>();
        List<String> roles = roleViewService.getRole();
        roles.forEach(r -> {
            map.put("role", r);
        });
        return map;
    }
}
