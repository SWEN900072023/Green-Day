package edu.unimelb.swen90007.mes.security;

import edu.unimelb.swen90007.mes.model.Administrator;
import edu.unimelb.swen90007.mes.service.impl.AppUserServiceImpl;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry -> registry
                .requestMatchers("/index.jsp").permitAll()
                .requestMatchers("/api/login", "/api/register").permitAll()
                .requestMatchers("/hello-servlet").hasRole(Administrator.class.getSimpleName())
                .anyRequest().authenticated());

        http.exceptionHandling(cfg -> cfg
                // Customize unauthenticated request handler
                .authenticationEntryPoint((request, response, authException) ->
                        ResponseWriter.write(response, 401, "unauthenticated"))
                // Customize unauthorized request handler
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        ResponseWriter.write(response, 403, "unauthorized"))
        );

        // Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(new AppUserServiceImpl())
                .passwordEncoder(new BCryptPasswordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // Customize login response
        http.authenticationManager(authenticationManager);
        http.formLogin(login -> login
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) ->
                                ResponseWriter.write(response, 200, "successfully logged in"))
                        .failureHandler((request, response, exception) ->
                                ResponseWriter.write(response, 401, "unauthenticated"))
                        .loginProcessingUrl("/api/login"))
                .logout(logout -> logout
                        .logoutSuccessHandler((request, response, authentication) ->
                                ResponseWriter.write(response, 200, "successfully logged out"))
                        .logoutUrl("/api/logout"));

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(System.getProperty("cors.origins.ui")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
