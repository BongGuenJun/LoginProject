package penguindisco.loginproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("penguindisco.loginproject.mapper")
public class LoginProjectApplication {

    public static void main(String[] args) {

        SpringApplication.run(LoginProjectApplication.class, args);
    }

}
