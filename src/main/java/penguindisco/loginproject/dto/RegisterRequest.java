package penguindisco.loginproject.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String id;
    private String password;
    private String email;
    private String phone;
    private String name;
    private String zipcode;
    private String address1;
    private String address2;
    private String loginType = "LOCAL";
    private String providerId;
}