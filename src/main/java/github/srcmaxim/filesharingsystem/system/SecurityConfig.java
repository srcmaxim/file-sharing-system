package github.srcmaxim.filesharingsystem.system;

import github.srcmaxim.filesharingsystem.service.UserPrincipalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static github.srcmaxim.filesharingsystem.model.Authority.ROLE_ADMIN;
import static github.srcmaxim.filesharingsystem.model.Authority.ROLE_USER;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserPrincipalsService userPrincipalsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/static/**", "/").permitAll()
                    .antMatchers("/register", "/login", "/logout").permitAll()
                    .antMatchers("/users/**").hasAuthority(ROLE_ADMIN.name())
                    .antMatchers("/resources/**").hasAnyAuthority(
                            ROLE_USER.name(), ROLE_ADMIN.name());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userPrincipalsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

}
