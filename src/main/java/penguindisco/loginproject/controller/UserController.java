package penguindisco.loginproject.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@SessionAttributes("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(Model model, @RequestParam("userId") String id,
                        @RequestParam("pass") String pass,
                        @RequestParam(name = "loginType", required = false, defaultValue = "LOCAL") LoginType loginType,
                        @RequestParam(name = "providerId", required = false) String providerId,
                        HttpSession session, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 타입에 따라 providerId 설정
        if (loginType == LoginType.LOCAL) {
            providerId = null;
        }

        int result = userService.login(id, pass, loginType, providerId);
        if (result == 1) { // 회원 아이디가 존재하지 않으면
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('존재하지 않는 아이디 입니다.');");
            out.println("history.back();");
            out.println("</script>");
            return null;
        } else if (result == 0) { // 비밀번호가 틀리면
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('비밀번호가 다릅니다.');");
            out.println("location.href='loginForm';");
            out.println("</script>");
            return null;
        }
        Users user = userService.getUser(id);
        session.setAttribute("isLogin", true);

        model.addAttribute("user", user);
        System.out.println("users.name : " + user.getName());

        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }

    @RequestMapping("/overlapIdCheck")
    public String overlapIdCheck(Model model, @RequestParam("id") String id) {

        //회원 아이디 중복 여부를 받아 온다.
        boolean overlap = userService.overlapIdCheck(id);

        //model에 회원 ID를 저장 및 중복 여부 저장
        model.addAttribute("id", id);
        model.addAttribute("overlap", overlap);

        return "user/overlapIdCheck.html";
    }

    @PostMapping("/joinResult")
    public String joinResult(Model model, Users user,
                             @RequestParam("pass1") String pass1,
                             @RequestParam("emailId") String emailId,
                             @RequestParam("emailDoamin") String emailDomain,
                             @RequestParam("mobile1") String mobile1,
                             @RequestParam("mobile2") String mobile2,
                             @RequestParam("mobile3") String mobile3,
                             @RequestParam("phone1") String phone1,
                             @RequestParam("phone2") String phone2,
                             @RequestParam("phone3") String phone3) {

        user.setPassword(pass1);
        user.setEmail(emailId + "@" + emailDomain);
        user.setPhone(mobile1 + "-" + mobile2 + "-" + mobile3);

        userService.insertUser(user);
        log.info("joinResult : " + user.getName());
        return "redirect:user/loginPage";

    }
}

