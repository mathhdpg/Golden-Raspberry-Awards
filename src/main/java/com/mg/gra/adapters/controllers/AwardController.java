package com.mg.gra.adapters.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mg.gra.adapters.dto.AwardIntervalResultDTO;
import com.mg.gra.adapters.mapper.AwardIntervalResultMapper;
import com.mg.gra.application.service.AwardService;
import com.mg.gra.domain.entity.AwardIntervalResult;

@RestController
@RequestMapping("/v1/award")
public class AwardController {

    private final AwardService awardService;
    private AwardIntervalResultMapper awardIntervalResultMapper;

    public AwardController(AwardService awardService, AwardIntervalResultMapper awardIntervalResultMapper) {
        this.awardService = awardService;
        this.awardIntervalResultMapper = awardIntervalResultMapper;
    }

    @RequestMapping("/min-max-intervals")
    public AwardIntervalResultDTO withAwardIntervals() {
        AwardIntervalResult producerWithAwardIntervals = awardService.withAwardIntervals();
        return awardIntervalResultMapper.toDTO(producerWithAwardIntervals);
    }

}