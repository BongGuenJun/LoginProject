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


    // 모든 사용자 조회
    List<Users> findAll();

    // 사용자 삽입
    void insertUser(Users user);

    // 사용자 정보 업데이트
    void updateUser(Users user);

    // 특정 UserNo로 사용자 삭제
    void deleteUser(@Param("userNo") Long userNo);




}
