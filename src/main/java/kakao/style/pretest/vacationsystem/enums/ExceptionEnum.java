package kakao.style.pretest.vacationsystem.enums;


import lombok.Getter;

@Getter
public enum ExceptionEnum implements EnumMapper{

    SOLD_OUT("E001","SOLD_OUT"),
    NOT_FOUND_EXCEPTION("E002","페이지를 찾을 수가 없습니다."),
    INTERNAL_SERVER_ERROR("E003","알 수 없는 에러가 발생 하였습니다.");

    private final String responseCode;
    private final String message;

    ExceptionEnum(String responseCode,String message){
        this.responseCode = responseCode;
        this.message = message;
    }

}
