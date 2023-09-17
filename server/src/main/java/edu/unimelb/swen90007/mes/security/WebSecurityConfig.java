package edu.unimelb.swen90007.mes.security;

import com.alibaba.fastjson.JSONObject;
import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.Administrator;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.service.impl.AppUserService;
import edu.unimelb.swen90007.mes.util.JwtUtil;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry -> registry
                        // unauthenticated access API
                        .requestMatchers(
                                "/index.jsp",
                                Constant.API_PREFIX + "/login",
                                Constant.API_PREFIX + "/register/customer"
                        ).permitAll()
                        // Administrator authority
                        .requestMatchers(
                                Constant.API_PREFIX + "/admin/**",
                                Constant.API_PREFIX + "/register/event-planner")
                        .hasAuthority(Administrator.class.getSimpleName())
                        // EventPlanner authority
                        .requestMatchers(Constant.API_PREFIX + "/planner/**")
                        .hasAuthority(EventPlanner.class.getSimpleName())
                        // Customer authority
                        .requestMatchers(Constant.API_PREFIX + "/customer/**")
                        .hasAuthority(Customer.class.getSimpleName())
                        // Any logged-in users
                        .anyRequest().authenticated())

                .formLogin(login -> login
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) ->
                                handleLoginSuccess(response, authentication))
                        .failureHandler((request, response, exception) ->
                                ResponseWriter.write(response, 401, "unauthenticated"))
                        .loginProcessingUrl(Constant.API_PREFIX + "/login"))
                // Use default setting for JWT validation
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

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
                .userDetailsService(new AppUserService())
                .passwordEncoder(new BCryptPasswordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    private void handleLoginSuccess(HttpServletResponse response, Authentication authentication)
            throws IOException {
        String jwtToken = createJwtToken(authentication);
        AppUser user = (AppUser) authentication.getPrincipal();
        String userType = user.getAuthorities().get(0).getAuthority();

        JSONObject data = new JSONObject();
        data.put("token", jwtToken);
        data.put("userId", user.getId());
        data.put("userType", userType);
        data.put("email", user.getEmail());
        data.put("firstName", user.getFirstName());
        data.put("lastName", user.getLastName());

        String msg = "Successfully logged in";
        ResponseWriter.write(response, 200, msg, data);
    }

    private String createJwtToken(Authentication authentication) {
        AppUser user = (AppUser) authentication.getPrincipal();
        Instant now = Instant.now();
        long expiry = Constant.JWT_EXPIRY;
        String authority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(Constant.JWT_ISSUER)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim(Constant.JWT_AUTHORITIES_CLAIM, authority)
                .claim(Constant.JWT_USER_ID_CLAIM, user.getId())
                .build();
        return jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtUtil.getInstance().getDecoder();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return JwtUtil.getInstance().getEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName(Constant.JWT_AUTHORITIES_CLAIM);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(System.getProperty("cors.origins.ui")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
