package kakao.style.pretest.vacationsystem.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
public class CommonResponse<T> {

    private String responseCode;
    private String message;
    private LocalDateTime requestTime;
    private T data;

    public CommonResponse(String responseCode, String message, LocalDateTime requestTime, T data) {
        this.responseCode = responseCode;
        this.message = message;
        this.requestTime = requestTime;
        this.data = data;
    }
}
