package penguindisco.loginproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.service.UserService;

import java.security.Principal;



@Controller
public class PageController {


    @GetMapping({"/" , "/main"})
    public String home() {
        return "views/main"; //main.html
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "views/login";
    }

    @GetMapping("/userJoin")
    public String userJoin() {
        return "user/userJoin"; // userjoin.html을 반환합니다.
    }



}