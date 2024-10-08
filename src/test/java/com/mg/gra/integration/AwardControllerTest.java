package com.mg.gra.integration;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.mg.gra.GraApplication;
import com.mg.gra.application.dto.AwardIntervalDTO;
import com.mg.gra.application.dto.AwardIntervalResultDTO;
import com.mg.gra.application.service.AwardService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest(classes = GraApplication.class)
@AutoConfigureMockMvc
public class AwardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AwardService awardService;

    @Test
    public void shouldReturnStatusOkAndCallService() throws Exception {
        when(awardService.withAwardIntervals())
                .thenReturn(new AwardIntervalResultDTO(new ArrayList<>(), new ArrayList<>()));

        mockMvc.perform(get("/v1/award/min-max-intervals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min").isEmpty())
                .andExpect(jsonPath("$.max").isEmpty());
    }

    @Test
    public void shouldReturnStatusOkWithConsistentMappedDTO() throws Exception {
        List<AwardIntervalDTO> mins = List.of(new AwardIntervalDTO("Producer1", 1, 2000, 2001));
        List<AwardIntervalDTO> maxs = List.of(new AwardIntervalDTO("Producer2", 99, 1900, 1999));
        AwardIntervalResultDTO resultDTO = new AwardIntervalResultDTO(mins, maxs);

        when(awardService.withAwardIntervals()).thenReturn(resultDTO);
        mockMvc.perform(get("/v1/award/min-max-intervals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.min[0].producer").value("Producer1"))
                .andExpect(jsonPath("$.min[0].interval").value(1))
                .andExpect(jsonPath("$.min[0].previousWin").value(2000))
                .andExpect(jsonPath("$.min[0].followingWin").value(2001))
                .andExpect(jsonPath("$.max[0].producer").value("Producer2"))
                .andExpect(jsonPath("$.max[0].interval").value(99))
                .andExpect(jsonPath("$.max[0].previousWin").value(1900))
                .andExpect(jsonPath("$.max[0].followingWin").value(1999))
                .andDo(print());
    }

    @Test
    public void shouldReturnServerErrorWhenServiceFails() throws Exception {
        when(awardService.withAwardIntervals()).thenThrow(new RuntimeException("Erro no serviço"));

        mockMvc.perform(get("/v1/award/min-max-intervals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}