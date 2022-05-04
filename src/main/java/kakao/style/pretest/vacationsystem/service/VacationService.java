package kakao.style.pretest.vacationsystem.service;

import kakao.style.pretest.vacationsystem.common.CommonResponse;
import kakao.style.pretest.vacationsystem.enums.ResponseEnum;
import kakao.style.pretest.vacationsystem.enums.VacationStatusEnum;
import kakao.style.pretest.vacationsystem.enums.VacationTypeEnum;
import kakao.style.pretest.vacationsystem.mapper.VacationMapper;
import kakao.style.pretest.vacationsystem.model.VacationDto;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationService {

    @Autowired
    VacationMapper mapper;

    LocalDateTime localDateTime;

    public VacationService(VacationMapper mapper){
        this.mapper = mapper;
    }

    /**
     * 내가 신청한 휴가 조회
     * @param userName
     * @return SuccessResponse<List<vacationDto>>
     */
    public CommonResponse<?> getVacationList(String userName){

        var data = mapper.findByIdVacations(userName)
                                    .stream()
                                    .peek(p -> {
                                        p.setRequestType(p.getRequestYn().equals("Y") ? VacationStatusEnum.REQUEST_COMPLETED : VacationStatusEnum.CANCEL_COMPLETED);

                                        if(p.getUseAnnualLeave() >= 1.0)
                                            p.setVacationType(VacationTypeEnum.FULL);
                                        else if(p.getUseAnnualLeave() == 0.5)
                                            p.setVacationType(VacationTypeEnum.HALF);
                                        else if(p.getUseAnnualLeave() == 0.25)
                                            p.setVacationType(VacationTypeEnum.HALF_AND_HALF);
                                    })
                                    .collect(Collectors.toList());
        /*내가 신청한 휴가 내역이 없을 때 처리*/
        if(data.size() == 0){
            return new CommonResponse(ResponseEnum.LIST_FAIL.getResponseCode(),ResponseEnum.LIST_FAIL.getMessage(),localDateTime.now(),data);
        }else{
            return new CommonResponse(ResponseEnum.LIST_SUCCESS.getResponseCode(),ResponseEnum.LIST_SUCCESS.getMessage(),localDateTime.now(),data);
        }
    }

    /**
     * 휴가신청
     * @param vacationDto
     * @param userName
     * @return CommonResponse<List<vacationDto>>
     */
    @Transactional
    public CommonResponse<?> saveVacation(VacationDto vacationDto,String userName){
        //현재 잔여 연차
        String remainingAnnualLeave = findByIdPresentAnnualLeave(userName);

        // 1. 내 연차가 남아 있지 않으면
        if( Float.parseFloat(remainingAnnualLeave) <= 0 || vacationDto.getUseAnnualLeave() > Float.parseFloat(remainingAnnualLeave) ){
            return new CommonResponse(ResponseEnum.NO_ANNUAL_LEAVE.getResponseCode()
                                        ,ResponseEnum.NO_ANNUAL_LEAVE.getMessage()
                                        ,localDateTime.now()
                                        ,ResponseEnum.REMAINING_ANNUAL_LEAVE.getMessage()+remainingAnnualLeave);
        }
        // 2. 현재날짜보다 이전날짜에 신청 할 경우
        if( !validChkDate(vacationDto.getStartedAt(),vacationDto.getEndedAt(),vacationDto.getUseAnnualLeave()) ){
            return new CommonResponse(ResponseEnum.ILLEGAL_DATE.getResponseCode()
                                        ,ResponseEnum.ILLEGAL_DATE.getMessage()
                                        ,localDateTime.now()
                                        ,ResponseEnum.REMAINING_ANNUAL_LEAVE.getMessage()+remainingAnnualLeave);
        }
        // 3. 신청 사용 연차가 0인 경우
        if( vacationDto.getUseAnnualLeave() == 0.0F){
            return new CommonResponse(ResponseEnum.REQUEST_ZERO.getResponseCode()
                    ,ResponseEnum.REQUEST_ZERO.getMessage()
                    ,localDateTime.now()
                    ,ResponseEnum.REMAINING_ANNUAL_LEAVE.getMessage()+remainingAnnualLeave);
        }

        mapper.saveVacation(vacationDto,userName);
        remainingAnnualLeave = ResponseEnum.REMAINING_ANNUAL_LEAVE.getMessage() + findByIdPresentAnnualLeave(userName);
        return new CommonResponse(ResponseEnum.REQUEST_SUCCESS.getResponseCode()
                                    ,ResponseEnum.REQUEST_SUCCESS.getMessage()
                                    ,localDateTime.now()
                                    ,remainingAnnualLeave);
    }

    /**
     * 연차 휴가 시작날짜/끝날짜 유효성 검사
     * @param startedAt
     * @param endedAt
     * @param useAnnualLeave
     * @return
     */
    public boolean validChkDate(LocalDate startedAt,LocalDate endedAt,float useAnnualLeave){
        LocalDate nowDate = LocalDate.of(localDateTime.now().getYear(),localDateTime.now().getMonth(),localDateTime.now().getDayOfMonth());
        if(useAnnualLeave >= 1.0)  //(연차)시작날짜가 현재날짜보다 이후 or 같고 끝 날짜는 현재날짜보다 이후인지
            return startedAt.isAfter(nowDate) || startedAt.isEqual(nowDate) && endedAt.isAfter(nowDate);
        else //(반차)시작 날짜가 현재날짜보다 이후 or 같은지
            return startedAt.isAfter(nowDate) || startedAt.isEqual(nowDate);
    }

    /**
     * 내 잔여연차 조회
     * @param userName
     * @return
     */
    public String findByIdPresentAnnualLeave(String userName){
        return mapper.findByIdPresentAnnualLeave(userName);
    }


    /**
     * 휴가등록 ID로
     * @param regId
     * @return
     */
    public List<VacationDto> findByRegIdVacation(String regId){
       return mapper.findByRegIdVacations(regId);
    }

    /**
     * 휴가 취소 처리
     * @param regId
     * @param userName
     * @return
     */
    @Transactional
    public CommonResponse<?> cancelVacation(String regId,String userName){

        if(findByRegIdVacation(regId).size() == 0)

            return new CommonResponse(ResponseEnum.CANCEL_NOT_FOUND.getResponseCode()
                    ,ResponseEnum.CANCEL_NOT_FOUND.getMessage()
                    ,localDateTime.now()
                    ,"");
        //휴가 신청 내역
        VacationDto cancelingVacation =  findByRegIdVacation(regId).get(0);
        //현재 잔여 연차
        String remainingAnnualLeave = findByIdPresentAnnualLeave(userName);

        if(!validChkDate(cancelingVacation.getStartedAt(),cancelingVacation.getEndedAt(),cancelingVacation.getUseAnnualLeave()))

            return new CommonResponse(ResponseEnum.CANCEL_ILLEGAL.getResponseCode()
                    ,ResponseEnum.CANCEL_ILLEGAL.getMessage()
                    ,localDateTime.now()
                    ,ResponseEnum.REMAINING_ANNUAL_LEAVE.getMessage() + remainingAnnualLeave);
        else

            mapper.cancelVacation(regId,userName); // 취소 처리
            return new CommonResponse(ResponseEnum.CANCEL.getResponseCode()
                    ,ResponseEnum.CANCEL.getMessage()
                    ,localDateTime.now()
                    ,ResponseEnum.REMAINING_ANNUAL_LEAVE.getMessage() + findByIdPresentAnnualLeave(userName));
    }


  /*  *//**
     * 휴가 유형이 FULL 인 경우
     * @param startedAt
     * @param endedAt
     * @return
     *//*
    public boolean validateHolidays(LocalDate startedAt, LocalDate endedAt){
        for (int i = startedAt.getDayOfMonth(); i <= endedAt.getDayOfMonth(); i++) {
        }

        return false;
    }

    *//**
     * 휴가 유형이 HALF / HALF and HALF 인 경우
     * @param startedAt
     * @return
     *//*
    public boolean validateHoliday(LocalDate startedAt){
        startedAt.getDayOfMonth();


        return false;
    }*/


}
