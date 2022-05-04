package kakao.style.pretest.vacationsystem.model;


import kakao.style.pretest.vacationsystem.enums.VacationStatusEnum;
import kakao.style.pretest.vacationsystem.enums.VacationTypeEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationDto {

    private int reqId;
    private String userName;
    private String comment;
    private float useAnnualLeave;
    private String requestYn;
    private LocalDate startedAt;
    private LocalDate endedAt;

    private VacationTypeEnum vacationType;
    private VacationStatusEnum requestType;

    @Override
    public String toString() {
        return "VacationDto{" +
                "reqId=" + reqId +
                ", userName='" + userName + '\'' +
                ", comment='" + comment + '\'' +
                ", useAnnualLeave=" + useAnnualLeave +
                ", requestYn='" + requestYn + '\'' +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                '}';
    }
}
