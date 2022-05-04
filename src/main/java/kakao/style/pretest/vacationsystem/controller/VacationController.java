package kakao.style.pretest.vacationsystem.controller;


import kakao.style.pretest.vacationsystem.common.CommonResponse;
import kakao.style.pretest.vacationsystem.model.VacationDto;
import kakao.style.pretest.vacationsystem.service.VacationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VacationController {

    @Autowired
    private VacationService vacationService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * 나의 전체 휴가신청 목록조회
     * @param userName
     * @return
     */
    @GetMapping("/vacation/list/{userName}")
    public ResponseEntity getVacationList(@PathVariable("userName") String userName) {
        logger.info("==> log {}", "getVacationList");
        CommonResponse<?> returnData = vacationService.getVacationList(userName);

        if (returnData.getResponseCode().equals("S0002")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnData);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(returnData);
        }
    }

    /**
     * 나의 휴가신청하기
     * @param vacationDto
     * @param userName
     * @return
     */
    @PostMapping("/vacation/register/{userName}")
    public ResponseEntity requestVacation(@RequestBody VacationDto vacationDto,@PathVariable("userName") String userName){
        logger.info("==> log {}","requestVacation");
        CommonResponse<?> returnData = vacationService.saveVacation(vacationDto,userName);

        if(returnData.getResponseCode().equals("E0012") || returnData.getResponseCode().equals("E0013") || returnData.getResponseCode().equals("E0014"))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnData);
        else
            return ResponseEntity.status(HttpStatus.OK).body(returnData);
    }

    /**
     * 내가 신청한 휴가취소하기
     * @param userName
     * @param regId
     * @return
     */
    @PutMapping("/vacation/cancel/{userName}/{regId}")
    public ResponseEntity cancelVacation(@PathVariable("regId") String regId,@PathVariable("userName") String userName){
        logger.info("==> log {}","cancelVacation");
        CommonResponse<?> returnData = vacationService.cancelVacation(regId,userName);

        if(returnData.getResponseCode().equals("E0022") || returnData.getResponseCode().equals("E0023"))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnData);
        else
            return ResponseEntity.status(HttpStatus.OK).body(returnData);
    }

}
