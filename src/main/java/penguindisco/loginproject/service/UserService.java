package penguindisco.loginproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import penguindisco.loginproject.domain.LoginType;
import penguindisco.loginproject.domain.Users;
import penguindisco.loginproject.dto.RegisterRequest;
import penguindisco.loginproject.repository.UserRepository;


import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void registerUser(RegisterRequest request) {
        Users user = new Users();
        user.setId(request.getUserId());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getUserPw()));
        user.setEmail(request.getUserEmail());
        user.setName(request.getUserName());
        user.setPhone(request.getPhone());
        user.setPhoneVerify(request.getPhoneVerifiy());
        user.setZipcode(request.getUserZipcode());
        user.setAddress1(request.getUserAddress1());
        user.setAddress2(request.getUserAddress2());
        user.setLoginType(LoginType.LOCAL);
        user.setProviderId(request.getProviderId());
        user.setPoint(0);
        userRepository.save(user);
    }

    public int login(String id, String pass){
        int result = -1;
        Users user = userRepository.findByUserNo(id).orElse(null);
        //id가 존재하지 않으면 : -1
        if (user == null){
            return result;
        }
        // 로그인 성공 : 1
        // 패스워드 인코더 를 통해 암호가 맞는지 확인
        if (passwordEncoder.matches(pass, user.getPassword())) {
            result = 1;
        }else {
            result = 0;
        }
        return result;
        }
    }

    public Optional<Users> findByUserNo(Long userNo) {
        return userRepository.findByUserNo(userNo);
    }

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public void updateUser(Users user) {
        userRepository.save(user);
    }

    public void deleteUser(Long userNo) {
        userRepository.deleteById(userNo);
    }
}

public void main() {
}