package top.kanetah.planH.interceptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.kanetah.planH.service.AdminService;
import top.kanetah.planH.service.CheckMobileService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PlanHLoginInterceptor implements HandlerInterceptor, InitializingBean {

    private String userCodePrefix;
    private final AdminService adminService;
    private final CheckMobileService checkMobileService;

    @Autowired
    public PlanHLoginInterceptor(
            AdminService adminService,
            CheckMobileService checkMobileService
    ) {
        this.adminService = adminService;
        this.checkMobileService = checkMobileService;
    }

    @Override
    public void afterPropertiesSet(
    ) throws Exception {
        userCodePrefix = adminService.getUserCodePrefix();
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) throws Exception {
        response.addCookie(new Cookie(
                "userCodePrefix", userCodePrefix
        ));
        response.addCookie(new Cookie(
                "checkMobile",
                String.valueOf(checkMobileService.check(request))
        ));
    }
}