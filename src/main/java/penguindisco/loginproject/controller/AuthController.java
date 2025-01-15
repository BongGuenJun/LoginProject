package penguindisco.loginproject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.dto.RegisterRequest;
import penguindisco.loginproject.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        log.info("/api/auth/register - User registered: " + request.getEmail());
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/user-info")
    public String userInfoPage(@AuthenticationPrincipal Object principal, Model model) {
        if (principal instanceof OAuth2User) {
            // 소셜 로그인 사용자 처리
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            Users user = userService.findByEmail(email);

            if (user == null) {
                String name = oAuth2User.getAttribute("name");
                String providerId = oAuth2User.getName();
                user = userService.saveSocialUser(email, name, providerId, LoginType.valueOf(oAuth2User.getAttribute("provider")));
            }

            model.addAttribute("user", user);

        } else if (principal instanceof UserDetails) {
            // 일반 로그인 사용자 처리
            UserDetails userDetails = (UserDetails) principal;
            String name = userDetails.getUsername();
            Users user = userService.findByName(name);

            if (user != null) {
                model.addAttribute("user", user);
            } else {
                System.out.println("No user found with name: " + name);
            }

        } else {
            // 인증되지 않은 경우 로그인 페이지로 리다이렉트
            log.info("/user-info");
            System.out.println("No authenticated user found.");

            return "redirect:/login";
        }

        log.info("/user-info");
        return "user/userInfo"; // 인증된 사용자 정보 페이지 반환
    }


}