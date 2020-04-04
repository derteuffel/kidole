package com.derteuffel.kidole.security;

import com.derteuffel.kidole.services.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by user on 23/03/2020.
 */
@Configuration
@Order(1)
public class CoordinatorSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CompteService compteService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/coordinator/**").authorizeRequests()
                .antMatchers("/coordinator/**").access("hasAnyRole('ROLE_ROOT','ROLE_COORDINATOR')")
                .and()
                .formLogin()
                .loginPage("/coordinator/login")
                .loginProcessingUrl("/coordinator/login/process")
                .defaultSuccessUrl("/coordinator/home")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/coordinator/logout"))
                .logoutSuccessUrl("/coordinator/login?logout")
                .and()
                .exceptionHandling().accessDeniedPage("/coordinator/access-denied");
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private DaoAuthenticationProvider authenticationProvider;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/js/**",
                        "/css/**",
                        "/img/**",
                        "/vendor/**",
                        "/fonts/**",
                        "/images/**",
                        "/static/**"
                        );
    }
}
