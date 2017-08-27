package top.kanetah.planH.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

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
        registry.addViewController("/admin/").setViewName("admin");
        registry.addViewController("/index/").setViewName("index");
        registry.addRedirectViewController(
                "/css/bootstrap.css.map",
                "/css/bootstrap.min.css.map"
        );
    }

    @Bean
    public ObjectMapper ObjectMapper(){
        return new ObjectMapper();
    }
}
