package com.example.demo.security;

import com.example.demo.auth.ApplicationUserService;
import com.example.demo.jwt.JwtConfig;
import com.example.demo.jwt.JwtTokenVerifier;
import com.example.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, SecretKey secretKey, JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.DELETE,
                        "/api/**",
                        "/api/*").hasAnyAuthority(
                                    ApplicationUserPermission.EMPLOYEES_DELETE.getPermission(),
                                    ApplicationUserPermission.DEPARTMENTS_DELETE.getPermission(),
                                    ApplicationUserPermission.LAPTOPS_DELETE.getPermission()
                                    )
                .antMatchers(
                        HttpMethod.POST,
                        "/api/**",
                        "/api/*")
                    .hasAnyAuthority(
                                    ApplicationUserPermission.EMPLOYEES_CREATE.getPermission(),
                                    ApplicationUserPermission.LAPTOPS_CREATE.getPermission(),
                                    ApplicationUserPermission.DEPARTMENTS_CREATE.getPermission()
                    )
                .antMatchers(
                        HttpMethod.PUT,
                        "/api/**",
                        "/api/*")
                    .hasAnyAuthority(
                            ApplicationUserPermission.EMPLOYEES_UPDATE.getPermission(),
                            ApplicationUserPermission.LAPTOPS_UPDATE.getPermission(),
                            ApplicationUserPermission.DEPARTMENTS_UPDATE.getPermission()
                    )
                .antMatchers(HttpMethod.GET, "/api/**", "/api/*").hasAnyAuthority(
                        ApplicationUserPermission.EMPLOYEES_READ.getPermission(),
                        ApplicationUserPermission.LAPTOPS_READ.getPermission(),
                        ApplicationUserPermission.DEPARTMENTS_READ.getPermission()
                )
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}