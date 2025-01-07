package penguindisco.loginproject.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.service.UserService;

import java.io.IOException;

@Controller
@SessionAttributes("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

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
            model.addAttribute("user", user);
            log.info("User logged in: {}", user.getName());
            return "redirect:/main";
        } catch (IllegalArgumentException e) {
            log.error("Login error: {}", e.getMessage());
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("<script>alert('" + e.getMessage() + "'); history.back();</script>");
            return null;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }

    @GetMapping("/overlapIdCheck")
    public String overlapIdCheck(Model model, @RequestParam("id") String id) {
        boolean isDuplicate = userService.overlapIdCheck(id);
        model.addAttribute("id", id);
        model.addAttribute("overlap", isDuplicate);
        return "user/overlapIdCheck";
    }

    @PostMapping("/joinResult")
    public String joinResult(Model model, Users user,
                             @RequestParam("pass1") String pass1,
                             @RequestParam("emailId") String emailId,
                             @RequestParam("emailDomain") String emailDomain,
                             @RequestParam("mobile1") String mobile1,
                             @RequestParam("mobile2") String mobile2,
                             @RequestParam("mobile3") String mobile3) {

        log.info("joinResult called with user ID: {}", user.getId());

        try {
            user.setPassword(pass1);
            user.setEmail(emailId + "@" + emailDomain);
            user.setPhone(String.format("%s-%s-%s", mobile1, mobile2, mobile3));

            userService.insertUser(user);
            log.info("User registered: {}", user.getName());
            return "redirect:/loginPage";
        } catch (Exception e) {
            log.error("Error registering user: ", e);
            model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");
            return "userJoin";
        }
    }
}