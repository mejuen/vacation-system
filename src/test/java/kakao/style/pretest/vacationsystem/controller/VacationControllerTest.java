package kakao.style.pretest.vacationsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.style.pretest.vacationsystem.enums.VacationTypeEnum;
import kakao.style.pretest.vacationsystem.model.VacationDto;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VacationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private WebApplicationContext ctx;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("신청한 휴가 목록 조회 성공 테스트")
    void getVacationList() throws Exception {
        mockMvc.perform(get("/api/vacation/list/{userName}", "kims"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상세 휴가 정보 확인 실패 테스트")
    void getVacationInfo2() throws Exception {
        mockMvc.perform(get("/api/vacation/list/{userName}", "100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("휴가 등록 요청 성공 테스트")
    void registerVacation() throws Exception {
        VacationDto vacationDto = VacationDto.builder()
                .startedAt(LocalDate.of(2022, 05, 05))
                .endedAt(LocalDate.of(2022, 05, 06))
                .comment("개인사")
                .useAnnualLeave(1.0F)
                .vacationType(VacationTypeEnum.FULL)
                .build();

        String data = mapper.writeValueAsString(vacationDto);

        mockMvc.perform(post("/api/vacation/register/{userName}", "kims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("휴가 등록시 연차 초과로 실패 테스트")
    void registerVacation2() throws Exception {
        VacationDto vacationDto = VacationDto.builder()
                .startedAt(LocalDate.of(2022, 05, 05))
                .endedAt(LocalDate.of(2022, 05, 20))
                .comment("개인사")
                .useAnnualLeave(15.0F)
                .vacationType(VacationTypeEnum.FULL)
                .build();

        String data = mapper.writeValueAsString(vacationDto);

        mockMvc.perform(post("/api/vacation/register/{userName}", "kims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isNotAcceptable());
    }


    @Test
    @DisplayName("휴가 등록시 지난 날짜 요청으로 실패 테스트")
    void registerVacation3() throws Exception {
        VacationDto vacationDto = VacationDto.builder()
                .startedAt(LocalDate.of(2022, 05, 03))
                .endedAt(LocalDate.of(2022, 05, 05))
                .comment("개인사")
                .useAnnualLeave(1.0F)
                .vacationType(VacationTypeEnum.FULL)
                .build();

        String data = mapper.writeValueAsString(vacationDto);

        mockMvc.perform(post("/api/vacation/register/{userName}", "kims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("휴가 등록시 사용 연차가 0 인 경우 실패 테스트")
    void registerVacation4() throws Exception {
        VacationDto vacationDto = VacationDto.builder()
                .startedAt(LocalDate.of(2022, 05, 04))
                .endedAt(LocalDate.of(2022, 05, 05))
                .comment("개인사")
                .useAnnualLeave(0.0F)
                .vacationType(VacationTypeEnum.FULL)
                .build();

        String data = mapper.writeValueAsString(vacationDto);

        mockMvc.perform(post("/api/vacation/register/{userName}", "kims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("휴가 취소 성공 테스트")
    void cancelVacation1() throws Exception {
        mockMvc.perform(put("/api/vacation/cancel/{userName}/{regId}", "kims",1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("휴가 취소시 존재 하지 않는 휴가 내역으로 요청시 실패 테스트")
    void cancelVacation2() throws Exception {
        mockMvc.perform(put("/api/vacation/cancel/{userName}/{regId}", "kims",6))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("휴가 취소시 지난 날짜 요청으로 실패 테스트")
    void cancelVacation3() throws Exception {
        mockMvc.perform(put("/api/vacation/cancel/{userName}/{regId}", "kims",2))
                .andExpect(status().isNotAcceptable());
    }
}

