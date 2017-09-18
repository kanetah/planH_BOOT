package top.kanetah.planH.controller;

import org.springframework.web.bind.annotation.*;
import top.kanetah.planH.service.RoleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RestController
public class AuxiliaryAjaxController {

    @Value(value = "${kanetah.planH.infoClassPackageName}")
    private String infoClassPackageName;
    private final RoleViewService roleViewService;

    @Autowired
    public AuxiliaryAjaxController(RoleViewService roleViewService) {
        this.roleViewService = roleViewService;
    }

    @ResponseBody
    @RequestMapping(value = "/role/get")
    public List<String> getRole() {
        return roleViewService.getRole();
    }

    @ResponseBody
    @RequestMapping(value = "/shutdown", method = RequestMethod.POST)
    public void shutdown(){
        Runtime.getRuntime().exit(0);
    }
}
