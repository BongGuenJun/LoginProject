package penguindisco.loginproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/" , "/main"})
    public String home() {
        return "views/main"; //main.html
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "views/loginPage"; // loginPage.html
    }

    @GetMapping("/userJoin")
    public String userJoin() {
        return "user/userJoin"; // userjoin.html을 반환합니다.
    }



}