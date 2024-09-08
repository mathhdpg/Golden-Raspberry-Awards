package com.mg.gra.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.mg.gra.GraApplication;
import com.mg.gra.application.dto.AwardIntervalDTO;
import com.mg.gra.application.dto.AwardIntervalResultDTO;
import com.mg.gra.application.service.AwardService;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = GraApplication.class)
public class AwardServiceTest {

    @Autowired
    private AwardService awardService;

    @Test
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldReturnMinMaxIntervals() {
        AwardIntervalResultDTO resultDTO = awardService.withAwardIntervals();
        assertTrue(resultDTO.min().isEmpty(), "Expected min intervals to be empty");
        assertTrue(resultDTO.max().isEmpty(), "Expected max intervals to be empty");
    }

    @Test
    @Sql(scripts = {
            "/sql/common/clean_database.sql",
            "/sql/scenario_same_award_for_min_max.sql"
    })
    public void shouldReturnSameMinMaxIntervals() {
        AwardIntervalResultDTO awardIntervals = awardService.withAwardIntervals();

        assertEquals(1, awardIntervals.min().size(), "Expected exactly one min interval");
        assertEquals(1, awardIntervals.max().size(), "Expected exactly one max interval");

        checkAwardInterval(awardIntervals.min().get(0), "Producer1", 10, 2000, 2010);
        checkAwardInterval(awardIntervals.max().get(0), "Producer1", 10, 2000, 2010);
    }

    @Test
    @Sql(scripts = {
            "/sql/common/clean_database.sql",
            "/sql/scenario_different_award_for_min_max.sql"
    })
    public void shouldReturnDifferentMinMaxIntervals() {
        AwardIntervalResultDTO awardIntervals = awardService.withAwardIntervals();

        assertEquals(1, awardIntervals.min().size(), "Expected exactly one min interval");
        assertEquals(1, awardIntervals.max().size(), "Expected exactly one max interval");

        checkAwardInterval(awardIntervals.min().get(0), "Producer1", 1, 2000, 2001);
        checkAwardInterval(awardIntervals.max().get(0), "Producer2", 30, 1990, 2020);
    }

    @Test
    @Sql(scripts = {
            "/sql/common/clean_database.sql",
            "/sql/scenario_multiple_award_for_min_max_unordered_database_insert.sql"
    })
    public void shouldReturnMultipleMinMaxIntervals() {
        AwardIntervalResultDTO awardIntervals = awardService.withAwardIntervals();

        assertEquals(2, awardIntervals.min().size(), "Expected exactly two min intervals");
        assertEquals(2, awardIntervals.max().size(), "Expected exactly two max intervals");

        checkAwardInterval(awardIntervals.min().get(0), "Producer1", 1, 2000, 2001);
        checkAwardInterval(awardIntervals.min().get(1), "Producer2", 1, 2000, 2001);
        checkAwardInterval(awardIntervals.max().get(0), "Producer3", 30, 1990, 2020);
        checkAwardInterval(awardIntervals.max().get(1), "Producer4", 30, 1990, 2020);
    }

    private void checkAwardInterval(AwardIntervalDTO awardIntervalDTO, String producer, int interval, int previousWin,
            int followingWin) {
        assertEquals(producer, awardIntervalDTO.producer(), "Producer mismatch");
        assertEquals(interval, awardIntervalDTO.interval(), "Interval mismatch");
        assertEquals(previousWin, awardIntervalDTO.previousWin(), "Previous win year mismatch");
        assertEquals(followingWin, awardIntervalDTO.followingWin(), "Following win year mismatch");
    }

}