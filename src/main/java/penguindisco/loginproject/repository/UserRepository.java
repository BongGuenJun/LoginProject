package penguindisco.loginproject.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import penguindisco.loginproject.domain.Users;


public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUserNo(Long userNo);
}