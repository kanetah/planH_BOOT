package com.kanetah.planH.controller;

import com.kanetah.planH.info.TaskInfo;
import com.kanetah.planH.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            value = "/task/get",
            method = RequestMethod.POST
    )
    public Object[] getTask(
            @PathVariable(value = "userCode") String userCode,
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to
    ) {
        List<TaskInfo> taskInfos = userService.getTask(
                Integer.valueOf(from), Integer.valueOf(to), userCode);
        List<Map<String, Object>> list = new ArrayList<>();
        TaskInfo taskInfo;
        int i;
        for (i = 0; i < taskInfos.size(); ++i) {
            taskInfo = taskInfos.get(i);
            HashMap<String, Object> infoMap = new HashMap<>();
            infoMap.put("subject", taskInfo.getSubject());
            infoMap.put("title", taskInfo.getTitle());
            infoMap.put("content", taskInfo.getContent());
            infoMap.put("deadline", taskInfo.getDeadline());
            infoMap.put("submit", taskInfo.getSubmitDate());
            infoMap.put("path", taskInfo.getSubmitFilePath());
            list.add(infoMap);
        }
        return list.toArray();
    }
}
