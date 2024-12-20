package com.paj2ee.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
                    ).hasRole("USER")
                    .requestMatchers(
                        "/users",
                        "/hello-admin",

                        "/admin/users/{id}/enable",
                        "/admin/users/{id}/disable",

                        "/admin/books/create_book",
                        "/admin/books/update-book/{id}",
                        "/admin/books/delete-book/{id}",

                        "/admin/book-collections/create-collection",
                        "/admin/book-collections/update-collection/{id}",
                        "/admin/book-collections/delete-collection/{id}",

                        "/admin/books/{bookId}/book-collections/{bookCollectionId}/add",
                        "/admin/books/{bookId}/book-collections/{bookCollectionId}/remove",

                        "/admin/book-wrappers",
                        "/admin/book-wrappers/{id}",
                        "/admin/book-wrappers/create-wrapper",
                        "/admin/book-wrappers/{id}/update-wrapper",
                        "/admin/book-wrappers/{id}/delete-wrapper"
                    ).hasRole("ADMIN")
                    .requestMatchers(
                        "/dashboard-header",

                        "/books",
                        "/books{id}",

                        "/book-collections",
                        "/book-collections/{id}",

                        "/book-loans",
                        "/book-loans/{id}",
                        "/book-loans/create-book-loan",
                        "/book-loans/{id}/update-book-loan",
                        "/book-loans/{id}/delete-book-loan"
                    ).hasAnyRole("ADMIN", "USER")
                    .anyRequest()
                    .authenticated()
            )
            .httpBasic((configurer) -> {
                //noop
            })
            .cors(Customizer.withDefaults())
            .csrf((configurer) ->
                configurer
                    .disable()
            )
        ;

        return http.build();

    }
}
