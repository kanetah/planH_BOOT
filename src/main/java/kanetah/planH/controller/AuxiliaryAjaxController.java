package kanetah.planH.controller;

import kanetah.planH.info.Info;
import kanetah.planH.service.RoleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, Object> getRole() {

        Map<String, Object> map = new HashMap<>();
        List<String> roles = roleViewService.getRole();
        roles.forEach(r ->
                map.put("role", r));
        return map;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/info/task")
    public Map[] getTaskInfo() {

        Class clazz = Info.forName(infoClassPackageName + ".TaskInfo");
        Field[] fields = clazz.getDeclaredFields();
        HashMap[] maps = new HashMap[fields.length];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = new HashMap<String, String>();
            maps[i].put("field", fields[i].getName());
        }
        return maps;
    }
}
