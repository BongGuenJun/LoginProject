package penguindisco.loginproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.dto.RegisterRequest;
import penguindisco.loginproject.mapper.UserMapper;
import penguindisco.loginproject.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    //회원 정보를 회원 테이블에 저장하는 메서드
    public void insertUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info(user.getPassword());
        userMapper.insertUser(user);
    }

    // 회원 아이디 중복 확인
    public boolean overlapIdCheck(String id) {
        Users user = userMapper.getUsers(id);
        log.info("overlapIdCheck - user: " + user);
        return user != null;
    }

    // 회원가입 처리
    public void registerUser(RegisterRequest request) {
        Users user = new Users();
        user.setId(request.getUserId());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getUserPw()));
        user.setEmail(request.getUserEmail());
        user.setName(request.getUserName());
        user.setPhone(request.getPhone());
        user.setZipcode(request.getUserZipcode());
        user.setAddress1(request.getUserAddress1());
        user.setAddress2(request.getUserAddress2());
        user.setLoginType(LoginType.LOCAL);
        user.setProviderId(request.getProviderId());
        user.setPoint(0);
        userRepository.save(user);
    }

    // 로그인 처리
    public int login(String id, String pass, LoginType loginType, String providerId) {
        int result = -1;
        Users user;

        if (loginType == LoginType.LOCAL) {
            // 일반 로그인 처리
            Long userNo;
            try {
                userNo = Long.valueOf(id);
            } catch (NumberFormatException e) {
                return result; // id가 숫자가 아닌 경우 -1 반환
            }
            user = userRepository.findByUserNo(userNo).orElse(null);
        } else {
            // 소셜 로그인 처리
            user = userRepository.findByProviderIdAndLoginType(providerId, loginType).orElse(null);
        }

        // 사용자 존재 여부 확인
        if (user == null) {
            return result;
        }

        // 일반 로그인인 경우 패스워드 확인
        if (loginType == LoginType.LOCAL && !passwordEncoder.matches(pass, user.getPassword())) {
            return 0; // 비밀번호 불일치
        }

        return 1; // 로그인 성공
    }

    // 회원 id에 해당하는 회원정보를 읽어와 반환하는 메서드
    public Users getUser(String id) {
        return userMapper.getUsers(id);
    }

    // 회원 번호로 회원을 찾는 메서드
    public Optional<Users> findByUserNo(Long userNo) {
        return userRepository.findByUserNo(userNo);
    }

    // 이메일로 회원을 찾는 메서드
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 모든 회원을 찾는 메서드
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    // 회원 정보를 업데이트하는 메서드
    public void updateUser(Users user) {
        userRepository.save(user);
    }

    // 회원을 삭제하는 메서드
    public void deleteUser(Long userNo) {
        userRepository.deleteById(userNo);
    }
}