package com.paj2ee.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .securityMatcher("/**")
            .authorizeHttpRequests((configurer) ->
                configurer
                    .requestMatchers(
                        "/login",
                        "/register"
                    ).permitAll()
                    .requestMatchers(
                        "/hello"
                    ).permitAll()
                    .requestMatchers(
                        "/hello-admin"
                    ).hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
            )
            .httpBasic((configurer) -> {
                //noop
            })
            .csrf((configurer) ->
                configurer.ignoringRequestMatchers(
                    "/register",
                    "/login"
                )
            )
        ;

        return http.build();

    }
}
