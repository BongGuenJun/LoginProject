package penguindisco.loginproject.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.service.UserService;

import java.util.Optional;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-info")
    public String userInfoPage(@AuthenticationPrincipal Object principal, Model model) {
        if (principal instanceof OAuth2User) {
            // 소셜 로그인 사용자 처리
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            Optional<Users> user = userService.findByEmail(email);

            user.ifPresentOrElse(
                    value -> model.addAttribute("user", value),
                    () -> System.out.println("No user found with email: " + email)
            );

        } else if (principal instanceof UserDetails) {
            // 일반 로그인 사용자 처리
            UserDetails userDetails = (UserDetails) principal;
            String name = userDetails.getUsername(); // 일반 로그인 사용자의 이름을 가져옴
            Optional<Users> user = userService.findByName(name);

            user.ifPresentOrElse(
                    value -> model.addAttribute("user", value),
                    () -> System.out.println("No user found with name: " + name)
            );

        } else {
            // 인증되지 않은 경우 로그인 페이지로 리다이렉트
            System.out.println("No authenticated user found.");
            return "redirect:/loginPage";
        }

        return "user/userInfo"; // 인증된 사용자 정보 페이지 반환
    }
}
