package penguindisco.loginproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/" , "/main"})
    public String home() {
        return "main"; //main.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @GetMapping("/userjoin")
    public String userJoin() {
        return "userjoin"; // userjoin.html을 반환합니다.
    }



}