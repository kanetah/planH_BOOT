package com.kanetah.planH.config;

import com.kanetah.planH.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

/**
 * Created by kane on 2017/7/19.
 * <p>
 * Spring Security 设置
 */
@Configuration
@EnableWebSecurity
@ComponentScan("com.kanetah.planH.service")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserLoginService userLoginService;

    @Autowired
    public void setUserDetailsService(
            UserLoginService userLoginService
    ) {
        this.userLoginService = userLoginService;
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth
    ) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("123").roles("USER").and()
//                .withUser("u").password("123").roles("U").and()
//                .withUser("admin").password("456").roles("ADMIN");

        auth
                .userDetailsService(userLoginService);
    }

    @Override
    protected void configure(
            HttpSecurity http
    ) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .permitAll()

                .and()
                .logout()
                .logoutSuccessUrl("/")

                .and()
                .rememberMe()
                .tokenRepository(new InMemoryTokenRepositoryImpl())
                .tokenValiditySeconds(2419200)
                .key("planH_Key")

                .and()
                .authorizeRequests()
                .anyRequest()
//                .permitAll()
                .authenticated()

//                .and()
//                .requiresChannel()
//                .antMatchers("/poi").requiresSecure()
// 需要SSL证书
        ;
    }
}
