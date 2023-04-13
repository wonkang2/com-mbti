package com.commbti.global.config;

import com.commbti.global.security.handler.CustomFailureHandler;
import com.commbti.global.security.handler.CustomSuccessHandler;
import com.commbti.global.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.DispatcherType;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationService authenticationService;
    @Bean
    public CustomSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler(authenticationService);
    }
    @Bean
    public CustomFailureHandler customFailureHandler() {
        return new CustomFailureHandler(authenticationService);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(
                                List.of("*")
                        );
                        config.setAllowedMethods(
                                List.of("*")
                        );
                        return config;
                    };
                    c.configurationSource(source);
                });
        http

                .authorizeRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .antMatchers("/css/**", "/js/**", "/Users/**").permitAll()
                        .antMatchers("/admin/**").hasAnyRole("ADMIN")
                        .antMatchers("/bulletin-board/*/edit", "/bulletin-board/*/post").authenticated()
                        .antMatchers(HttpMethod.GET, "/bulletin-board/**", "/comments/**", "/login/**", "/").permitAll()
                        .antMatchers("/api/signup", "/signup/**", "/api/members").permitAll()
                        .anyRequest().authenticated());
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login-process")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(customSuccessHandler())
                .failureHandler(customFailureHandler());

        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/");

        return http.build();
    }

}
