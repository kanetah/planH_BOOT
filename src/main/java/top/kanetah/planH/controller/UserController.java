package top.kanetah.planH.controller;

import top.kanetah.planH.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/user/{userCode}")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/fetch",
            method = RequestMethod.POST
    )
    public Object[] getTask(
            @PathVariable(value = "userCode") String userCode,
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to
    ) throws InterruptedException {
        return
                userService.getTask(
                        Integer.valueOf(from),
                        Integer.valueOf(to),
                        Long.valueOf(userCode)
                ).toArray();
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/patch/{taskId}",
            method = RequestMethod.POST
    )
    public String submitTask(
            @PathVariable(value = "userCode") String userCode,
            @RequestPart(value = "file") MultipartFile file,
            @PathVariable(value = "taskId") String taskId
    ) throws IOException {
        userService.submitTask(
                Long.valueOf(userCode), Long.valueOf(taskId), file
        );
        return "[\"patched\"]";
    }

    @ResponseBody
    @RequestMapping(
            value = "/username",
            method = RequestMethod.POST
    )
    public String readUsername(
            @PathVariable(value = "userCode") String userCode
    ) {
        return "[\"" + userService.getUserName(Long.valueOf(userCode)) + "\"]";
    }
}
