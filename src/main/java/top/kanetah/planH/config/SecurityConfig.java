package top.kanetah.planH.config;

import top.kanetah.planH.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

@Configuration
@EnableWebSecurity
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
                .failureForwardUrl("/login?error=true")
                .permitAll()

                .and()
                .logout()
                .logoutSuccessUrl("/")

                .and()
                .rememberMe()
                .tokenRepository(new InMemoryTokenRepositoryImpl())
                .tokenValiditySeconds(10368000)
                .key("planH_Key")

                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**")
                .permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .requiresChannel()
                .anyRequest()
                .requiresSecure()
        ;
    }
}
