package penguindisco.loginproject.mapper;

import org.apache.ibatis.annotations.*;
import penguindisco.loginproject.domain.Users;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE user_no = #{userNo}")
    Users findByUserNo(Long userNo);

    @Select("SELECT * FROM users WHERE id = #{id}")
    Users findByUserId(String id);

    @Select("SELECT * FROM users")
    List<Users> findAll();

    @Insert("INSERT INTO users(id, password, email, phone, phone_verify, name, zipcode, address1, address2, login_type, provider_id, reg_date, point) " +
            "VALUES(#{id}, #{password}, #{email}, #{phone}, #{phoneVerify}, #{name}, #{zipcode}, #{address1}, #{address2}, #{loginType}, #{providerId}, #{regDate}, #{point})")
    @Options(useGeneratedKeys = true, keyProperty = "userNo")
    void insertUser(Users user);

    @Update("UPDATE users SET password = #{password}, email = #{email}, phone = #{phone}, phone_verify = #{phoneVerify}, name = #{name}, " +
            "zipcode = #{zipcode}, address1 = #{address1}, address2 = #{address2}, login_type = #{loginType}, provider_id = #{providerId}, " +
            "point = #{point} WHERE user_no = #{userNo}")
    void updateUser(Users user);

    @Delete("DELETE FROM users WHERE user_no = #{userNo}")
    void deleteUser(Long userNo);
}