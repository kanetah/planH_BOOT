package top.kanetah.planH.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.kanetah.planH.service.CheckMobileService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/user")
public class UserPageController {
    private final CheckMobileService checkMobileService;

    @Autowired
    public UserPageController(CheckMobileService checkMobileService) {
        this.checkMobileService = checkMobileService;
    }

    @RequestMapping(value = "/{userCode}")
    public String toUserPage(
            @PathVariable String userCode,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Cookie cookie = new Cookie("userCode", userCode);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.addCookie(new Cookie(
                "checkMobile",
                String.valueOf(checkMobileService.check(request))
        ));
        return "user";
    }
}
