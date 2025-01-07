package penguindisco.loginproject.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String id;         // 필드명 수정 (userId -> id)
    private String password;   // 필드명 수정 (userPw -> password)
    private String email;      // 필드명 수정 (userEmail -> email)
    private String phone;
    private String name;       // 필드명 수정 (userName -> name)
    private String zipcode;
    private String address1;
    private String address2;
    private String providerId;
}