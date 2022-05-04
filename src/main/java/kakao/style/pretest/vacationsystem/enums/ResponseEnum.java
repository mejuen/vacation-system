package kakao.style.pretest.vacationsystem.enums;


import lombok.Getter;
@Getter
public enum ResponseEnum implements EnumMapper {

    /*휴가조회*/
    LIST_SUCCESS("S0001","신청한 휴가 내역 조회 성공"),
    LIST_FAIL("S0002","신청하신 휴가 내역이 없습니다."),
    /*휴가신청*/
    REQUEST_SUCCESS("S0011","휴가신청이 완료되었습니다."),
    ILLEGAL_DATE("E0012","현재날짜보다 이전날짜로 휴가신청 할 수 없습니다."),
    NO_ANNUAL_LEAVE("E0013","잔여 연차가 없어서 휴가신청을 할 수 없습니다."),
    REQUEST_ZERO("E0014","신청 휴가가 0일 입니다."),
    REMAINING_ANNUAL_LEAVE("","현재 잔여연차 :"),
    /*휴가 취소시*/
    CANCEL("S0021","휴가신청 취소가 완료되었습니다."),
    CANCEL_ILLEGAL("E0022","현재날짜보다 이전날짜의 휴가는 취소할 수 없습니다."),
    CANCEL_NOT_FOUND("E0023","휴가신청 취소하려는 신청내역이 존재하지 않습니다."),
    /*공휴일에 휴가신청 할때*/
    HOLIDAY("E0003","선택하신 날짜에 공휴일이 존재 합니다. 공휴일을 제외하고 휴가신청 해주세요.");

    private final String responseCode;
    private final String message;

    ResponseEnum(String responseCode, String message){
        this.responseCode = responseCode;
        this.message = message;
    }

}
