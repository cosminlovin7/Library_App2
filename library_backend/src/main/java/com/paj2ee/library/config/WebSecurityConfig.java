package com.paj2ee.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Order(0)
    @Bean
    public  SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {

        http
            .securityMatcher("/actuator/**")
            .authorizeHttpRequests((configurer) ->
                configurer
                    .requestMatchers(
                        "/actuator/**"
                    ).authenticated()
                    .anyRequest().denyAll()
            )
            .httpBasic(Customizer.withDefaults())
            .authenticationManager(authenticationManagerForActuator())
        ;

        return http.build();
    }

    @Bean
    public UserDetailsService actuatorUserDetailsService() {
        // Define the username and password for Basic Authentication
        return username -> User
            .withUsername("actuator_username")
            .password(passwordEncoder().encode("actuator_password"))
            .roles("USER")
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerForActuator() throws Exception {
        return new ProviderManager(new AuthenticationProvider[] {
            new DaoAuthenticationProvider() {{
                setUserDetailsService(actuatorUserDetailsService());
                setPasswordEncoder(passwordEncoder());
            }}
        });
    }

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .securityMatcher("/**")
            .authorizeHttpRequests((configurer) ->
                configurer
                    .requestMatchers(
                        "/login",
                        "/register",
                        "/book-wrappers",
                        "/book-collections",
                        "/book-collections/{id}"
                    ).permitAll()
                    .requestMatchers(
                        "/hello"
                    ).hasRole("USER")
                    .requestMatchers(
                        "/users",
                        "/hello-admin",

                        "/admin/users/{id}/enable",
                        "/admin/users/{id}/disable",

                        "/admin/books",
                        "/admin/books/{id}",
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

                        "/book-loans",
                        "/book-loans/{id}",
                        "/book-loans/create-book-loan",
                        "/book-loans/{id}/update-book-loan",
                        "/book-loans/{id}/delete-book-loan",

                        "/users/{id}/book-loans",
                        "/users/{id}/update-username",
                        "/users/{id}/update-email",
                        "/users/{id}/update-phone-number"
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
