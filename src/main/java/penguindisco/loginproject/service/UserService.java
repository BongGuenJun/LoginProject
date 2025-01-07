package penguindisco.loginproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.dto.RegisterRequest;
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

    /**
     * 사용자 인증 처리
     *
     * @param id        사용자 ID
     * @param password  사용자 비밀번호
     * @param loginType 로그인 타입 (LOCAL or SOCIAL)
     * @return 인증된 사용자 객체
     * @throws IllegalArgumentException 인증 실패 시 예외
     */
    public Users authenticate(String id, String password, LoginType loginType) {
        Optional<Users> userOptional;

        if (loginType == LoginType.LOCAL) {
            userOptional = userRepository.findByName(id);
        } else {
            userOptional = userRepository.findByProviderIdAndLoginType(id, loginType);
        }

        Users user = userOptional.orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (loginType == LoginType.LOCAL && !passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    /**
     * 회원 정보를 저장하는 메서드
     * @param user 사용자 정보
     */
    @Transactional
    public void insertUser(Users user) {
        log.info("Inserting user: {}", user.getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("New user saved: {}", user.getName());
    }

    /**
     * 회원 ID 중복 확인
     * @param id 사용자 ID
     * @return 중복 여부
     */
    public boolean overlapIdCheck(String id) {
        return userRepository.findByName(id).isPresent();
    }

    /**
     * 회원 번호로 회원을 찾는 메서드
     * @param userNo 사용자 번호
     * @return 사용자 정보
     */
    public Optional<Users> findByUserNo(Long userNo) {
        return userRepository.findByUserNo(userNo);
    }

    /**
     * 회원 이름으로 회원을 찾는 메서드
     * @param name 사용자 이름
     * @return 사용자 정보
     */
    public Optional<Users> findByName(String name) {
        return userRepository.findByName(name);
    }

    /**
     * 이메일로 회원을 찾는 메서드
     * @param email 사용자 이메일
     * @return 사용자 정보
     */
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 회원가입 처리 메서드
     * @param request 회원가입 요청 정보
     */
    @Transactional
    public void registerUser(RegisterRequest request) {
        Users user = new Users();
        user.setId(request.getId()); // 필드명 수정
        user.setName(request.getName()); // 필드명 수정
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 필드명 수정
        user.setEmail(request.getEmail()); // 필드명 수정
        user.setPhone(request.getPhone());
        user.setZipcode(request.getZipcode()); // 필드명 수정
        user.setAddress1(request.getAddress1()); // 필드명 수정
        user.setAddress2(request.getAddress2()); // 필드명 수정
        user.setLoginType(LoginType.LOCAL); // 로그인 타입 설정
        user.setProviderId(request.getProviderId());

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getName());
    }

    /**
     * 모든 회원을 찾는 메서드
     * @return 모든 회원 정보
     */
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    /**
     * 회원 정보를 업데이트하는 메서드
     * @param user 사용자 정보
     */
    public void updateUser(Users user) {
        userRepository.save(user);
    }

    /**
     * 회원을 삭제하는 메서드
     * @param userNo 사용자 번호
     */
    public void deleteUser(Long userNo) {
        userRepository.deleteById(userNo);
    }
}