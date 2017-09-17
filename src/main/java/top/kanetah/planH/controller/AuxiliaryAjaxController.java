package top.kanetah.planH.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import top.kanetah.planH.info.Info;
import top.kanetah.planH.service.RoleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/info/task")
    public Map[] getTaskInfo() {

        Class clazz = Info.forName(infoClassPackageName + ".TaskInfo");
        Field[] fields = clazz.getDeclaredFields();
        HashMap[] fieldMap = new HashMap[fields.length];
        for (int i = 0; i < fieldMap.length; i++) {
            fieldMap[i] = new HashMap<String, String>();
            fieldMap[i].put("field", fields[i].getName());
        }
        return fieldMap;
    }

    @ResponseBody
    @RequestMapping(value = "/shutdown", method = RequestMethod.POST)
    public void shutdown(){
        Runtime.getRuntime().exit(0);
    }
}
