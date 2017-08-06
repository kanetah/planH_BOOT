package planH.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @RequestMapping(value = "/{userCode}")
    public String toUserPage(@PathVariable String userCode) {
        return "user";
    }
}
