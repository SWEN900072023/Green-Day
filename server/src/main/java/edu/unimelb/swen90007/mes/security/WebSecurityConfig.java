package edu.unimelb.swen90007.mes.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.Administrator;
import edu.unimelb.swen90007.mes.service.impl.AppUserService;
import edu.unimelb.swen90007.mes.util.RSAKeyReader;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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
                        .requestMatchers(
                                "/index.jsp",
                                Constant.API_PREFIX + "/login",
                                Constant.API_PREFIX + "/register/customer"
                        ).permitAll()
                        // Administrator permission
                        .requestMatchers(
                                "/hello-servlet",
                                Constant.API_PREFIX + "/register/event-planner"
                        ).hasRole(Administrator.class.getSimpleName())
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
                .logout(logout -> logout
                        .logoutSuccessHandler((request, response, authentication) ->
                                ResponseWriter.write(response, 200, "Successfully logged out"))
                        .logoutUrl(Constant.API_PREFIX + "/logout"))
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
        Instant now = Instant.now();
        long expiry = Constant.JWT_EXPIRY;
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(Constant.JWT_ISSUER)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim(Constant.JWT_AUTHORITIES_CLAIM_NAME, role)
                .build();
        return jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            RSAPublicKey publicKey = RSAKeyReader.readPublicKey();
            return NimbusJwtDecoder.withPublicKey(publicKey).build();
        } catch (Exception e) {
            logger.error("Error reading public key: " + e.getMessage());
            return null;
        }
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        try {
            RSAPublicKey publicKey = RSAKeyReader.readPublicKey();
            RSAPrivateKey privateKey = RSAKeyReader.readPrivateKey();
            JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
            JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
            return new NimbusJwtEncoder(jwks);
        } catch (Exception e) {
            logger.error("Error reading key pairs: " + e.getMessage());
            return null;
        }
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName(Constant.JWT_AUTHORITIES_CLAIM_NAME);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(System.getProperty("cors.origins.ui")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
