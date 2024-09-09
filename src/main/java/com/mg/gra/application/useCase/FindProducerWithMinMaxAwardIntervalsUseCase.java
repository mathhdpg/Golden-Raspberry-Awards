package com.mg.gra.application.useCase;

import java.util.ArrayList;
import java.util.List;

import com.mg.gra.application.gateways.ProducerGateway;
import com.mg.gra.domain.entity.AwardInterval;
import com.mg.gra.domain.entity.AwardIntervalResult;
import com.mg.gra.domain.entity.ProducerAward;

public class FindProducerWithMinMaxAwardIntervalsUseCase {

    private final ProducerGateway producerGateway;

    public FindProducerWithMinMaxAwardIntervalsUseCase(ProducerGateway producerGateway) {
        this.producerGateway = producerGateway;
    }

    @SuppressWarnings("null")
    public AwardIntervalResult findProducerWithAwardIntervals() {
        List<ProducerAward> awards = producerGateway.findAllWinningProducersOrderByProducerAndMovieYear();

        List<AwardInterval> mins = new ArrayList<>();
        List<AwardInterval> maxs = new ArrayList<>();

        String currentProducer = null;
        Integer previousWinYear = null;

        int maxInterval = Integer.MIN_VALUE;
        int minInterval = Integer.MAX_VALUE;

        for (ProducerAward award : awards) {
            if (!award.getProducerName().equals(currentProducer)) {
                currentProducer = award.getProducerName();
                previousWinYear = award.getMovieYear();
                continue;
            }

            int currentWinYear = award.getMovieYear();
            int interval = currentWinYear - previousWinYear;

            if (interval < minInterval) {
                mins.clear();
                mins.add(new AwardInterval(currentProducer, interval, previousWinYear, currentWinYear));
                minInterval = interval;
            } else if (interval == minInterval) {
                mins.add(new AwardInterval(currentProducer, interval, previousWinYear, currentWinYear));
            }

            if (interval > maxInterval) {
                maxs.clear();
                maxs.add(new AwardInterval(currentProducer, interval, previousWinYear, currentWinYear));
                maxInterval = interval;
            } else if (interval == maxInterval) {
                maxs.add(new AwardInterval(currentProducer, interval, previousWinYear, currentWinYear));
            }

            previousWinYear = currentWinYear;
        }

        return new AwardIntervalResult(mins, maxs);
    }

}