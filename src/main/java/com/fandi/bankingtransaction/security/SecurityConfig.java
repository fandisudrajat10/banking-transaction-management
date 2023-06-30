package com.fandi.bankingtransaction.security;

import com.fandi.bankingtransaction.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN = "ADMIN";
    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String CUSTOMER = "CUSTOMER";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new TokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/v1/admin/**").hasAuthority(ADMIN)
                .antMatchers("/api/v1/employee/**").hasAuthority(EMPLOYEE)
                .antMatchers("/api/v1/transaction/**").hasAuthority(CUSTOMER)
                .antMatchers(HttpMethod.POST,"/api/v1/account/change-password").hasAnyAuthority(ADMIN, EMPLOYEE, CUSTOMER)
                .antMatchers(HttpMethod.GET,"/api/v1/account/my-profile").hasAnyAuthority(ADMIN, EMPLOYEE, CUSTOMER)
                .antMatchers(HttpMethod.GET,"/api/v1/account/my-account").hasAuthority(CUSTOMER)
                .antMatchers(HttpMethod.POST,"/api/v1/account/customer-register").hasAnyAuthority(ADMIN, EMPLOYEE)
                .antMatchers(HttpMethod.PUT,"/api/v1/account/customer-update").hasAnyAuthority(ADMIN, EMPLOYEE)
                .antMatchers(HttpMethod.DELETE,"/api/v1/account/delete/**").hasAuthority(ADMIN)
                .antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"errors\":\"" + authException.getMessage() + "\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"errors\":\"" + accessDeniedException.getMessage() + "\"}");
                })
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
