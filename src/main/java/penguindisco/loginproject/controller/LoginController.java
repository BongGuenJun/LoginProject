package penguindisco.loginproject.controller;



import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.service.UserService;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-info")
    public String userInfoPage(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User != null) {
            // 소셜 로그인 사용자의 경우
            String email = oAuth2User.getAttribute("email");
            Optional<Users> user = userService.findByEmail(email);
            user.ifPresent(value -> model.addAttribute("user", value));
        } else {
            // 일반 사용자의 경우 (테스트 중이므로 null 처리)
            model.addAttribute("user", null);
        }
        return "user/userInfo"; // user-info.html
    }
}