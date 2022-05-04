package kakao.style.pretest.vacationsystem.service;

import kakao.style.pretest.vacationsystem.config.TokenProvider;
import kakao.style.pretest.vacationsystem.mapper.UserMapper;
import kakao.style.pretest.vacationsystem.model.LoginDto;
import kakao.style.pretest.vacationsystem.model.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
public class UserService {

   private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginDto loginDto) {
        UserDto userDto = userMapper.findUserByUserName(loginDto.getUserName())
                .orElseThrow(() -> new RuntimeException("잘못된 아이디 입니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), userDto.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다");
        }
        return tokenProvider.createToken(userDto.getUserId(), Collections.singletonList(userDto.getRole()));
    }

}
