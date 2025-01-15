package penguindisco.loginproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.service.UserService;

@Slf4j
@Controller
public class joinController {

    @Autowired
    private UserService userService;


    // 아이디 중복 확인
    @GetMapping("/overlapIdCheck")
    public String overlapIdCheck(Model model, @RequestParam("id") String id) {
        if (id == null || id.trim().isEmpty()) {
            log.warn("Invalid ID received: {}", id);
            model.addAttribute("errorMessage", "아이디를 입력해주세요.");
            return "user/overlapIdCheck"; // 에러 메시지를 표시하는 페이지 또는 동일 페이지 반환
        }

        log.info("Received request for overlapIdCheck with ID: {}", id);
        boolean isDuplicate = userService.overlapIdCheck(id); // 중복 체크
        log.info("Is duplicate: {}", isDuplicate);

        model.addAttribute("id", id);
        model.addAttribute("overlap", isDuplicate);
        return "user/overlapIdCheck"; // 경로 수정 완료
    }



    // 회원가입 처리
    @PostMapping("/joinResult")
    public String joinResult(
            Model model,
            @RequestParam("name") String name,
            @RequestParam("id") String id,
            @RequestParam("pass1") String pass1,
            @RequestParam("pass2") String pass2,
            @RequestParam("emailId") String emailId,
            @RequestParam("emailDomain") String emailDomain,
            @RequestParam("mobile1") String mobile1,
            @RequestParam("mobile2") String mobile2,
            @RequestParam("mobile3") String mobile3,
            @RequestParam("zipcode") String zipcode,
            @RequestParam("address1") String address1,
            @RequestParam("address2") String address2,
            @RequestParam(value = "emailGet", required = false, defaultValue = "false") boolean emailGet
    ) {
        try {
            // 비밀번호 확인
            if (!pass1.equals(pass2)) {
                model.addAttribute("errorMessage", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                return "userJoin"; // 다시 회원가입 페이지로 이동
            }

            // 이메일 조합
            String email = emailId + "@" + emailDomain;

            // 모바일 번호 조합
            String mobile = mobile1 + "-" + mobile2 + "-" + mobile3;

            // Users 객체 생성 및 설정
            Users user = new Users();
            user.setName(name);
            user.setId(id);
            user.setPassword(pass1); // 원시 비밀번호 설정
            user.setEmail(email);
            user.setPhone(mobile);
            user.setZipcode(zipcode);
            user.setAddress1(address1);
            user.setAddress2(address2);
            user.setLoginType(LoginType.LOCAL); // 기본 로그인 타입 설정
            user.setProviderId(null); // 소셜 로그인 사용하지 않음

            // 명확히 설정: pass는 pass1
            log.info("Setting pass field with pass1 value: {}", pass1);
            user.setPassword(pass1);

            log.info("User to insert: {}", user);

            // DB 저장 처리
            userService.insertUser(user);

            log.info("User successfully registered");
            return "redirect:/loginPage";
        } catch (Exception e) {
            log.error("Error during registration: ", e);
            model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");
            return "userJoin"; // 오류 발생 시 회원가입 페이지로 이동
        }
    }


}
