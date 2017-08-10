package com.kanetah.planH.controller;

import com.kanetah.planH.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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
    ) {
        return
                userService.getTask(
                        Long.valueOf(from),
                        Long.valueOf(to),
                        userCode
                ).toArray();
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/patch",
            method = RequestMethod.POST
    )
    public Map<String, Object> submitTask(
            @PathVariable(value = "userCode") String userCode,
            @RequestPart(value = "file") MultipartFile file,
            @RequestParam(value = "taskId") String taskId
    ) throws IOException {
        return
                userService.submitTask(
                        Long.valueOf(userCode),
                        Long.valueOf(taskId),
                        file
                );
    }
}
