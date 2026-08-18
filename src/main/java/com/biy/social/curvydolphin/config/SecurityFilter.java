package com.biy.social.curvydolphin.config;

import com.biy.social.curvydolphin.exceptions.ApiError;
import com.biy.social.curvydolphin.service.AuthorizationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.biy.social.curvydolphin.constants.ErrorMessages.AUTHORIZATION_INVALID;

@Configuration
@Profile("dev")
@Slf4j
public class SecurityFilter {
    @Autowired
    AuthorizationService authorizationService;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauthUser =
                                    (OAuth2User) authentication.getPrincipal();
                            // convert to token to get provider smh
                            OAuth2AuthenticationToken token =
                                    (OAuth2AuthenticationToken) authentication;
                            // get token provider
                            String provider = token.getAuthorizedClientRegistrationId();
                            authorizationService.createOrUpdateAccount(provider, oauthUser);

                            response.sendRedirect("http://localhost:5173/");
                        })
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // BREAKPOINT HERE
                            System.out.println("Authentication failed!");
                            System.out.println(
                                    authException.getClass().getName()
                            );
//                            throw new InsufficientAuthenticationException("Authentication Failed");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");

                            response.getWriter().write(MAPPER.writeValueAsString(getError()));
                        }))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        }))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()); // reconsider for production; see note below

        return http.build();
    }

    public Map<String, Object> getError(){
        Map<String, Object> body = new HashMap<>();
        body.put("status", AUTHORIZATION_INVALID.getStatus());
        body.put("error_code", AUTHORIZATION_INVALID.getError_code());
        body.put("error", AUTHORIZATION_INVALID.getError());
        body.put("message", AUTHORIZATION_INVALID.getError());

        return body;
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setAllowCredentials(true); // required so the session cookie is sent
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
