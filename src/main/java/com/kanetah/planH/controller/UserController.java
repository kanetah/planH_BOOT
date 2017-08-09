package com.kanetah.planH.controller;

import com.kanetah.planH.info.TaskInfo;
import com.kanetah.planH.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            value = "/task/get"
//            method = RequestMethod.POST
    )
    public Map<String, Object> getTask(
//            @PathVariable(value = "userCode") String userCode,
//            @RequestParam(value = "from") String from,
//            @RequestParam(value = "to") String to
    ) {
        List<TaskInfo> taskInfos = userService.getTask(0,0,"");
//                Integer.valueOf(from), Integer.valueOf(to), userCode);
        Map<String, Object> map = new HashMap<>();
        TaskInfo taskInfo;
        for (int i = 0; i < taskInfos.size(); ) {
            taskInfo = taskInfos.get(i);
            HashMap<String, Object> infoMap = new HashMap<>();
            infoMap.put("subject", taskInfo.getSubject());
            infoMap.put("title", taskInfo.getTitle());
            infoMap.put("content", taskInfo.getContent());
            infoMap.put("deadline", taskInfo.getDeadline());
            infoMap.put("submit", taskInfo.getSubmitDate());
            infoMap.put("path", taskInfo.getSubmitFilePath());
            map.put(String.valueOf(i++), infoMap);
        }
        return map;
    }
}
