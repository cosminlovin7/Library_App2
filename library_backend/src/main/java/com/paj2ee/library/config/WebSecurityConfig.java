package com.paj2ee.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
//@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            .authorizeHttpRequests((configurer) ->
                configurer
//                    .requestMatchers(
//                        "/login",
//                        "/login?error",
//                        "/login/process"
//                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin((configurer) ->
                configurer
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .failureUrl("/login?error")
                    .successForwardUrl("/")
                    .loginProcessingUrl("/login/process")
            )
        ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

}
