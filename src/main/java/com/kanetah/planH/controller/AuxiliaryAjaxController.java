package com.kanetah.planH.controller;

import com.kanetah.planH.service.RoleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuxiliaryAjaxController {

    private final RoleViewService roleViewService;

    @Autowired
    public AuxiliaryAjaxController(RoleViewService roleViewService) {
        this.roleViewService = roleViewService;
    }

    @ResponseBody
    @RequestMapping(value = "/role/get")
    public Map<String, Object> get() {

        Map<String, Object> map = new HashMap<>();
        List<String> roles = roleViewService.getRole();
        roles.forEach(r ->
                map.put("role", r));
        return map;
    }
}
