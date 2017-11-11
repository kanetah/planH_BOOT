package top.kanetah.planH.controller;

import org.springframework.web.bind.annotation.*;
import top.kanetah.planH.service.AdminService;
import top.kanetah.planH.service.RoleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import top.kanetah.planH.service.SendMailService;
import top.kanetah.planH.service.UserService;

import java.io.IOException;
import java.util.List;

@RestController
public class AuxiliaryAjaxController {

    @Value(value = "${kanetah.planH.infoClassPackageName}")
    private String infoClassPackageName;
    private final RoleViewService roleViewService;
    private final SendMailService sendMailService;
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public AuxiliaryAjaxController(
            RoleViewService roleViewService,
            SendMailService sendMailService,
            UserService userService,
            AdminService adminService
    ) {
        this.roleViewService = roleViewService;
        this.sendMailService = sendMailService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @ResponseBody
    @RequestMapping(value = "/role/get")
    public List<String> getRole() {
        return roleViewService.getRole();
    }

    @ResponseBody
    @RequestMapping(value = "/subject/names")
    public Object[] getSubjectNames() throws IOException {
        return sendMailService.getSubjectNames();
    }

    @ResponseBody
    @RequestMapping(value = "/processor/values")
    public Object[] getProcessorNames() throws IOException {
        return userService.getProcessorValues();
    }

    @ResponseBody
    @RequestMapping(value = "/timers")
    public Object[] getTimers() {
        return sendMailService.getTimers();
    }

    @ResponseBody
    @RequestMapping(
            value = "/sendMail/{taskId}",
            method = RequestMethod.GET
    )
    public String sendMail(
            @PathVariable(value = "taskId") String taskId
    ) throws IOException {
        try {
            sendMailService.sendMail(taskId);
            return "[\"succeed\"]";
        } catch (NumberFormatException e) {
            return "[\"exception task " + taskId + "\"]";
        }
    }

    @ResponseBody
    @RequestMapping(
            value = "/delete/{taskId}",
            method = RequestMethod.GET
    )
    public String deleteTask(
            @PathVariable(value = "taskId") String taskId
    ) throws IOException {
        try {
            adminService.deleteTask(Long.valueOf(taskId));
            return "[\"succeed\"]";
        } catch (NumberFormatException e) {
            return "[\"exception task " + taskId + "\"]";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/shutdown", method = RequestMethod.POST)
    public void shutdown(){
        Runtime.getRuntime().exit(0);
    }
}
