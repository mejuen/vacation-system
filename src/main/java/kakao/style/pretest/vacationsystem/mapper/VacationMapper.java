package kakao.style.pretest.vacationsystem.mapper;


import kakao.style.pretest.vacationsystem.model.VacationDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VacationMapper {

    /**
     * 나의 전체 휴가신청 목록조회
     */
    @Select("SELECT" +
            " REQ_ID" +
            ",USER_NAME" +
            ",COMMENT" +
            ",USE_ANNUAL_LEAVE" +
            ",STARTED_AT" +
            ",ENDED_AT" +
            ",REQUEST_YN" +
            " FROM VACATION" +
            " WHERE USER_NAME = #{userName}" +
            " AND REQUEST_YN = 'Y'"
            )
    public List<VacationDto> findByIdVacations(@Param("userName") String userName); //,@Param("reqId") String reqId

    /**
     * 휴가 취소할 휴가 조회
     */
    @Select("SELECT" +
            " REQ_ID" +
            ",USER_NAME" +
            ",COMMENT" +
            ",USE_ANNUAL_LEAVE" +
            ",STARTED_AT" +
            ",ENDED_AT" +
            ",REQUEST_YN" +
            " FROM VACATION" +
            " WHERE REQ_ID = #{reqId}" +
            " AND REQUEST_YN = 'Y'"
    )
    public List<VacationDto> findByRegIdVacations(@Param("reqId") String reqId);

    /**
    * 휴가신청 -> 휴가신청하는 id,사용일수,시작일,종료일(반차/반반차 필요없음),코멘트(선택항목)
    *  String userName
    *  float useAnnualLeave
    *  startedAt
    *  endedAt
    *  comment
    * */
    @Insert("INSERT INTO VACATION (USER_NAME, COMMENT, USE_ANNUAL_LEAVE,REQUEST_YN, STARTED_AT,ENDED_AT)" +
            " VALUES (#{userName},#{dto.comment},#{dto.useAnnualLeave},'Y', #{dto.startedAt}, #{dto.endedAt})")
    public int saveVacation(@Param("dto") VacationDto vacationDto,@Param("userName") String userName);


    /**
     * 휴가신청취소 -> 신청한 휴가를 취소 처리한다.
     *  내가 신청한 휴가 id 를 요청플래그 컬럼 값을 요청 안함으로 변경
     *@Param userName
     * */
    @Update("UPDATE VACATION SET REQUEST_YN='N' WHERE USER_NAME = #{userName} AND REQ_ID = #{reqId}")
    public int cancelVacation(@Param("reqId") String reqId,@Param("userName") String userId);

    /**
     *  현재 나의 잔여 연차 조회
     *@param userName
     * */
    @Select("SELECT" +
            " U.ANNUAL_LEAVE - SUM(VA.USE_ANNUAL_LEAVE)  AS PRESENT_ANNUAL_LEAVE" +
            " FROM VACATION VA, USER U" +
            " WHERE VA.USER_NAME = U.USER_NAME" +
            " AND VA.REQUEST_YN = 'Y'" +
            " AND U.USER_NAME = #{userName}"+
            " GROUP BY U.ANNUAL_LEAVE")
    public String findByIdPresentAnnualLeave(@Param("userName") String userName);


}
