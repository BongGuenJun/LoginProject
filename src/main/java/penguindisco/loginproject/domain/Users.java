package penguindisco.loginproject.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private Long userNo;
    private String id;
    private String password;
    private String email;
    private String phone;
    private Integer phoneVerify; // 알람수신여부(1: yes)
    private String name;
    private String zipcode;
    private String address1;
    private String address2;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String providerId;
    private LocalDateTime regDate;
    private Integer point = 0;
}