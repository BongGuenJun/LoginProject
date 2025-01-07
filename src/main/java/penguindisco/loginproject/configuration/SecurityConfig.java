package penguindisco.loginproject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import penguindisco.loginproject.service.CustomOAuth2UserService;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/userJoin", "/loginPage", "/oauth2/**", "/register", "/oauth2.0/*", "/overlapIdCheck").permitAll()
                        .requestMatchers("/static/**", "/bootstrap/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/loginPage")
                        .defaultSuccessUrl("/") // 일반 로그인 성공 후 메인 페이지 이동
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/loginPage")
                        .defaultSuccessUrl("/main") // 소셜 로그인 성공 후 메인 페이지 이동
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            // 로그인 성공 시 세션 초기화
                            request.getSession().setAttribute("isLoginOk", true);
                            response.sendRedirect("/main");
                        })
                        .failureHandler((request, response, exception) -> {
                            // 실패 시 메인 페이지로 이동
                            exception.printStackTrace(); // 실패 이유 로그 출력
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
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/loginPage")
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                        .addLogoutHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                System.out.println("User " + authentication.getName() + " logged out.");
                            }
                        })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
