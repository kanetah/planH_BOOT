package top.kanetah.planH.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.*;
import top.kanetah.planH.interceptor.PlanHLoginInterceptor;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final PlanHLoginInterceptor loginInterceptor;

    @Autowired
    public MvcConfig(PlanHLoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer
    ) {
        configurer.enable();
    }

    @Override
    public void addViewControllers(
            ViewControllerRegistry registry
    ) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/admin/").setViewName("admin_x");
    }

    @Override
    public void addInterceptors(
            InterceptorRegistry registry
    ) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/login");
    }

    @Bean
    public ObjectMapper ObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("1024MB");
        factory.setMaxRequestSize("1056MB");
        return factory.createMultipartConfig();
    }
}
