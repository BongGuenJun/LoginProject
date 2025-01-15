package penguindisco.loginproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.service.UserService;

@Slf4j
@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-info")
    public String userInfoPage(@AuthenticationPrincipal Object principal, Model model) {
        Users user = null;

        if (principal instanceof OAuth2User) {
            // 소셜 로그인 사용자 처리
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            user = userService.findByEmail(email);

            if (user == null) {
                log.info("No user found with email: {}", email);
                throw new UsernameNotFoundException("User not found with email: " + email);
            }

        } else if (principal instanceof UserDetails) {
            // 일반 로그인 사용자 처리
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername(); // 일반 로그인 사용자의 ID를 가져옴
            user = userService.findById(username); // ID로 사용자 정보 조회

            if (user == null) {
                log.info("No user found with ID: {}", username);
                throw new UsernameNotFoundException("User not found with ID: " + username);
            }

        } else {
            // 인증되지 않은 경우 로그인 페이지로 리다이렉트
            log.info("No authenticated user found.");
            return "redirect:/loginPage";
        }

        model.addAttribute("user", user);
        log.info("User info page accessed");
        return "user/userInfo"; // 인증된 사용자 정보 페이지 반환
    }
}