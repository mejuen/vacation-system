package kakao.style.pretest.vacationsystem.controller;

import kakao.style.pretest.vacationsystem.common.CommonResponse;
import kakao.style.pretest.vacationsystem.model.LoginDto;
import kakao.style.pretest.vacationsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        ResponseEntity responseEntity = null;
        try {
            String token = userService.login(loginDto);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token);
            CommonResponse<?> response  = new CommonResponse("SL000","로그인성공", LocalDateTime.now(),token);
            responseEntity = ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(response);

        } catch (RuntimeException exception) {
            logger.debug(exception.getMessage());
            CommonResponse<?> response  = new CommonResponse("FL000","로그인실패", LocalDateTime.now(),"");
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return responseEntity;
    }
}
