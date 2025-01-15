package penguindisco.loginproject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import penguindisco.loginproject.service.CustomOAuth2UserService;
import penguindisco.loginproject.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(UserService userService, CustomOAuth2UserService customOAuth2UserService) {
        this.userService = userService;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "user/**", "/userJoin", "/login", "/oauth2/**", "/register", "/oauth2.0/*", "/overlapIdCheck").permitAll()
                        .requestMatchers("/static/**", "/bootstrap/**", "/css/**", "/js/**", "/images/**", "/joinResult", "/h2-console/**", "/userInfo").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/main") // 일반 로그인 성공 후 메인 페이지 이동
                        .failureUrl("/login?error=true")
                        .successHandler((request, response, authentication) -> {
                            log.info("로그인 성공");
                            response.sendRedirect("/main");
                        })
                        .failureHandler((request, response, exception) -> {
                            log.info("로그인 실패");
                            response.sendRedirect("/login?error=true");
                        })
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/main") // 소셜 로그인 성공 후 메인 페이지 이동
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            log.info("로그인 성공");
                            response.sendRedirect("/main");
                        })
                        .failureHandler((request, response, exception) -> {
                            log.info("로그인 실패");
                            response.sendRedirect("/main?error=true&message=Login%20issue%20occurred.%20Redirecting%20to%20main.");
                        })
                )
                .sessionManagement(session -> session
                        .sessionFixation().newSession() // 새로운 세션 생성
                        .maximumSessions(1) // 최대 세션 수
                        .maxSessionsPreventsLogin(false) // 이전 세션 만료 후 새 세션 허용
                        .expiredSessionStrategy(event -> {
                            System.out.println("Session expired for: " + event.getSessionInformation().getPrincipal());
                        })
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}