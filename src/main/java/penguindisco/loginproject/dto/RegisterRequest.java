package penguindisco.loginproject.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userId;
    private String userPw;
    private String userEmail;
    private String userName;
    private String phone;
    private Integer phoneVerifiy;  // 변경된 부분
    private String userZipcode;
    private String userAddress1;
    private String userAddress2;
    private String providerId;
}