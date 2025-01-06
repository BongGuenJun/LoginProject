package penguindisco.loginproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import penguindisco.loginproject.domain.Users;

import java.util.List;

@Mapper
public interface UserMapper {

    Users getUsers(@Param("id") String id);

    Users findByUserNo(@Param("userNo") Long userNo);

    Users findByUserId(@Param("userId") String userId);

    List<Users> findAll();

    void insertUser(Users user);

    void updateUser(Users user);

    void deleteUser(@Param("userNo") Long userNo);
}