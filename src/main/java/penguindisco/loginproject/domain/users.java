package penguindisco.loginproject.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import Domain.LoginType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    private String id;

    private String password;

    private String email;

    private String phone;

    private Integer phoneVerifiy; // 알람수신여부(1: yes)

    private String name;

    private String zipcode;

    private String address1;

    private String address2;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String providerId;

    @CreationTimestamp
    private LocalDateTime regDate;

    private Integer point = 0;
}