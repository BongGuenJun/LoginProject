package penguindisco.loginproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUserNo(Long userNo);
    Optional<Users> findByName(String Name);
    Optional<Users> findByProviderIdAndLoginType(String providerId, LoginType loginType);
}