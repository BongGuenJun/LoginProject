package penguindisco.loginproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import penguindisco.loginproject.domain.Users;

import java.util.List;

@Mapper
public interface UserMapper {

    // 특정 ID로 사용자 조회
     Users findById(@Param("id") String id);

    // 특정 UserNo로 사용자 조회
    Users findByUserNo(@Param("userNo") Long userNo);

    // 이름으로 사용자 조회
    Users findByName(@Param("name") String name);

    //이메일로 사용자 조회
    Users findByEmail(@Param("email") String email);

    //로그인 타입으로 사용자 조회
    Users findByLoginType(@Param("loginType") String loginType);

    // 모든 사용자 조회
    List<Users> findAll();

    // 사용자 삽입
    void insertUser(Users user);

    // 사용자 정보 업데이트
    void updateUser(Users user);

    // 특정 UserNo로 사용자 삭제
    void deleteUser(@Param("userNo") Long userNo);

}
