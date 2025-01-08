package penguindisco.loginproject.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.dto.RegisterRequest;
import penguindisco.loginproject.service.UserService;

import java.io.IOException;

@Controller
@SessionAttributes("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 로그인 처리
    @PostMapping("/login")
    public String login(Model model,
                        @RequestParam("userId") String id,
                        @RequestParam("pass") String pass,
                        @RequestParam(name = "loginType", required = false, defaultValue = "LOCAL") LoginType loginType,
                        HttpSession session, HttpServletResponse response) throws IOException {

        log.info("Login attempt with ID: {}", id);

        try {
            Users user = userService.authenticate(id, pass, loginType); // 예외 기반 인증 처리
            session.setAttribute("isLogin", true);
            session.setAttribute("user", user);
            log.info("User logged in: {}", user.getName());
            return "redirect:/main";
        } catch (IllegalArgumentException e) {
            log.error("Login error: {}", e.getMessage());
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("<script>alert('" + e.getMessage() + "'); history.back();</script>");
            return null;
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }

    // 아이디 중복 확인
    @GetMapping("/overlapIdCheck")
    public String overlapIdCheck(Model model, @RequestParam("id") String id) {
        boolean isDuplicate = userService.overlapIdCheck(id);
        model.addAttribute("id", id);
        model.addAttribute("overlap", isDuplicate);
        return "user/overlapIdCheck";
    }

    // 회원가입 처리
    @PostMapping("/joinResult")
    public String joinResult(@ModelAttribute RegisterRequest registerRequest, Model model) {
        log.info("joinResult called with user ID: {}", registerRequest.getId());

        try {
            Users user = new Users();
            user.setId(registerRequest.getId());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 암호화
            user.setEmail(registerRequest.getEmail());
            user.setPhone(registerRequest.getPhone());
            user.setName(registerRequest.getName());
            user.setZipcode(registerRequest.getZipcode());
            user.setAddress1(registerRequest.getAddress1());
            user.setAddress2(registerRequest.getAddress2());
            user.setLoginType(LoginType.valueOf(registerRequest.getLoginType().toUpperCase()));
            user.setProviderId(registerRequest.getProviderId());

            log.info("User to insert: {}", user);
            userService.insertUser(user);
            log.info("User successfully registered");
            return "redirect:/loginPage";
        } catch (Exception e) {
            log.error("Error during registration: ", e);
            model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");
            return "userJoin";
        }
    }
}
