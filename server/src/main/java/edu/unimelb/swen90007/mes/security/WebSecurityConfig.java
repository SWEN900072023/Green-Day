package edu.unimelb.swen90007.mes.security;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.Administrator;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.service.impl.AppUserService;
import edu.unimelb.swen90007.mes.util.JwtUtil;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final Logger logger = LogManager.getLogger(WebSecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry -> registry
                        // public API
                        .requestMatchers(HttpMethod.GET,
                                "/index.jsp"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                Constant.API_PREFIX + "/login",
                                Constant.API_PREFIX + "/register/customer"
                        ).permitAll()
                        // Administrator permission
                        .requestMatchers(HttpMethod.GET,
                                "/hello-servlet"
                        ).hasAuthority(Administrator.class.getSimpleName())
                        .requestMatchers(HttpMethod.POST,
                                Constant.API_PREFIX + "/register/event-planner"
                        ).hasAuthority(Administrator.class.getSimpleName())
                        // EventPlanner permission
                        .requestMatchers(HttpMethod.POST, Constant.API_PREFIX + "/events")
                        .hasAuthority(EventPlanner.class.getSimpleName())
                        // Any logged-in users
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            String jwtToken = createJwtToken(authentication);
                            String msg = "Successfully logged in";
                            ResponseWriter.write(response, 200, msg, jwtToken);
                        })
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
