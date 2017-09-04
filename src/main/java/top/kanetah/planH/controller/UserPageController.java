package top.kanetah.planH.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/user")
public class UserPageController {

    @RequestMapping(value = "/{userCode}")
    public String toUserPage(
            @PathVariable String userCode,
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("userCode", userCode);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "user_x";
    }
}
