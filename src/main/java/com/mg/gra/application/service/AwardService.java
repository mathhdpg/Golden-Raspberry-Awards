package com.mg.gra.application.service;

import org.springframework.stereotype.Service;

import com.mg.gra.application.useCase.FindProducerWithMinMaxAwardIntervalsUseCase;
import com.mg.gra.domain.entity.AwardIntervalResult;

@Service
public class AwardService {

    private final FindProducerWithMinMaxAwardIntervalsUseCase findProducerWithAwardIntervalsUseCase;

    public AwardService(
            FindProducerWithMinMaxAwardIntervalsUseCase findProducerWithAwardIntervalsUseCase) {

        this.findProducerWithAwardIntervalsUseCase = findProducerWithAwardIntervalsUseCase;
    }

    public AwardIntervalResult withAwardIntervals() {
        return findProducerWithAwardIntervalsUseCase
                .findProducerWithAwardIntervals();
    }

}