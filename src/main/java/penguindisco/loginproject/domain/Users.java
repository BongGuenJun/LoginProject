package penguindisco.loginproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity // JPA 엔티티로 표시
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 위한 전략 지정
    private Long userNo;
    private String id;
    private String password;
    private String email;
    private String phone;
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