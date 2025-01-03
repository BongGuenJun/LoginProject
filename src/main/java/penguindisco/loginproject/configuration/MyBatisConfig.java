package penguindisco.loginproject.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "loginproject.Mapper")
public class MyBatisConfig {
}