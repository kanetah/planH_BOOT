package com.kanetah.planH.controller;

import com.kanetah.planH.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        List<Map<String, Object>> ajaxList = userService.getTask(
                Long.valueOf(from), Long.valueOf(to), userCode);
        return ajaxList.toArray();
    }

    @ResponseBody
    @RequestMapping(
            value = "/task/patch",
            method = RequestMethod.POST
    )
    public Map<String, Object> submitTask(
            @PathVariable(value = "userCode") String userCode,
            @RequestPart(value = "file") MultipartFile file,
            @RequestParam(value = "taskId") String id
    ) throws IOException {
        System.out.println(userCode);
        System.out.println(id);
        System.out.println(file.getSize());

        System.out.println("开始");
        String path = "D:/temp/aaa";
        String fileName = file.getOriginalFilename();
        System.out.println(path);
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        targetFile = new File(path, fileName);
        if(!targetFile.exists()) {
            targetFile.createNewFile();
        }
        // 保存
        file.transferTo(targetFile);

        Map<String, Object> map = new HashMap<>();
        map.put("status", "patched");
        return map;
    }
}
