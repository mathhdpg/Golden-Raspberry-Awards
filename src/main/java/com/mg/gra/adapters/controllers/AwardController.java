package com.mg.gra.adapters.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mg.gra.application.dto.AwardIntervalResultDTO;
import com.mg.gra.application.service.AwardService;

@RestController
@RequestMapping("/v1/award")
public class AwardController {

    private final AwardService awardService;

    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    @RequestMapping("/min-max-intervals")
    public AwardIntervalResultDTO withAwardIntervals() {
        return awardService.withAwardIntervals();
    }

}