package top.kanetah.planH.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import top.kanetah.planH.interceptor.PlanHLoginInterceptor;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final PlanHLoginInterceptor loginInterceptor;

    @Autowired
    public MvcConfig(
            PlanHLoginInterceptor loginInterceptor,
            TomcatServletWebServerFactory tomcatServletWebServerFactory
    ) {
        this.loginInterceptor = loginInterceptor;
        tomcatServletWebServerFactory.addErrorPages(new ErrorPage("/error"));
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
        registry.addViewController("/error").setViewName("error");
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

//    @Bean
//    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
//        return new EmbeddedServletContainerCustomizer() {
//            @Override
//            public void customize(ConfigurableEmbeddedServletContainer container) {
//                container.setSessionTimeout(1, TimeUnit.MINUTES);
//            }
//        };
//    }
//    @Bean
//    public ConfigurableEmbeddedServletContainer containerCustomizer() {
//
//        return (container -> {
//            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
//            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
//            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
//
//            container.addErrorPages(error401Page, error404Page, error500Page);
//        });
//    }
}
