package com.mg.gra.application.service;

import org.springframework.stereotype.Service;

import com.mg.gra.application.dto.AwardIntervalResultDTO;
import com.mg.gra.application.mapper.AwardIntervalResultMapper;
import com.mg.gra.application.useCase.FindProducerWithMinMaxAwardIntervalsUseCase;
import com.mg.gra.domain.models.AwardIntervalResult;

@Service
public class AwardService {

    private final AwardIntervalResultMapper awardIntervalResultMapper;
    private final FindProducerWithMinMaxAwardIntervalsUseCase findProducerWithAwardIntervalsUseCase;

    public AwardService(
            FindProducerWithMinMaxAwardIntervalsUseCase findProducerWithAwardIntervalsUseCase,
            AwardIntervalResultMapper awardIntervalResultMapper) {

        this.awardIntervalResultMapper = awardIntervalResultMapper;
        this.findProducerWithAwardIntervalsUseCase = findProducerWithAwardIntervalsUseCase;
    }

    public AwardIntervalResultDTO withAwardIntervals() {
        AwardIntervalResult producerWithAwardIntervals = findProducerWithAwardIntervalsUseCase
                .findProducerWithAwardIntervals();

        return awardIntervalResultMapper.toDTO(producerWithAwardIntervals);
    }

}