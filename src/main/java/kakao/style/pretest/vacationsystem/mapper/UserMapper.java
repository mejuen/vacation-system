package kakao.style.pretest.vacationsystem.mapper;

import kakao.style.pretest.vacationsystem.model.LoginDto;
import kakao.style.pretest.vacationsystem.model.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("SELECT" +
            " USER_ID" +
            ",USER_NAME" +
            ",PASSWORD" +
            ",ANNUAL_LEAVE" +
            ",ROLE" +
            " FROM USER" +
            " WHERE" +
            " USER_NAME = #{userName}")
    Optional<UserDto> findUserByUserName(@Param("userName") String userName);

/*    @Select("SELECT" +
            " USER_ID" +
            ",USER_NAME" +
            ",PASSWORD" +
            ",ANNUAL_LEAVE" +
            ",ROLE" +
            " FROM USER" +
            " WHERE" +
            " USER_ID = #{userId}")
    Optional<UserDto> findByUserId(@Param("userId") Long userId);*/
}
