package com.mg.gra.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mg.gra.GraApplication;
import com.mg.gra.application.useCase.FindProducerWithMinMaxAwardIntervalsUseCase;
import com.mg.gra.domain.gateways.ProducerGateway;
import com.mg.gra.domain.models.AwardInterval;
import com.mg.gra.domain.models.AwardIntervalResult;
import com.mg.gra.domain.models.ProducerAward;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = GraApplication.class)
public class FindProducerWithAwardIntervalsUseCaseTest {

    @Mock
    private ProducerGateway producerGateway;

    private FindProducerWithMinMaxAwardIntervalsUseCase useCase;

    private List<ProducerAward> awards;

    @BeforeEach
    public void setup() {
        awards = new ArrayList<>();
        useCase = new FindProducerWithMinMaxAwardIntervalsUseCase(producerGateway);
    }

    @Test
    public void testEmptyList() {
        when(producerGateway.findAllWinningProducersOrderByProducerAndMovieYear()).thenReturn(awards);

        AwardIntervalResult result = useCase.findProducerWithAwardIntervals();

        assertTrue(result.min().isEmpty());
        assertTrue(result.max().isEmpty());
    }

    @Test
    public void testSingleAward() {
        ProducerAward award = new ProducerAward("Producer1", 2020);
        awards.add(award);
        when(producerGateway.findAllWinningProducersOrderByProducerAndMovieYear()).thenReturn(awards);

        AwardIntervalResult result = useCase.findProducerWithAwardIntervals();

        assertTrue(result.min().isEmpty());
        assertTrue(result.max().isEmpty());
    }

    @Test
    public void testMultipleAwardsSameProducer() {
        ProducerAward award1 = new ProducerAward("Producer1", 2010);
        ProducerAward award2 = new ProducerAward("Producer1", 2015);
        awards.add(award1);
        awards.add(award2);
        when(producerGateway.findAllWinningProducersOrderByProducerAndMovieYear()).thenReturn(awards);

        AwardIntervalResult result = useCase.findProducerWithAwardIntervals();

        assertEquals(1, result.min().size());
        assertEquals(1, result.max().size());

        checkAwardInterval(award1, award2, result.min().get(0));
        checkAwardInterval(award1, award2, result.max().get(0));
    }

    @Test
    public void testMultipleAwardsDifferentProducers() {
        ProducerAward award1 = new ProducerAward("Producer1", 2010);
        ProducerAward award2 = new ProducerAward("Producer2", 2015);
        awards.add(award1);
        awards.add(award2);
        when(producerGateway.findAllWinningProducersOrderByProducerAndMovieYear()).thenReturn(awards);

        AwardIntervalResult result = useCase.findProducerWithAwardIntervals();

        assertTrue(result.min().isEmpty());
        assertTrue(result.max().isEmpty());
    }

    @Test
    public void testAwardsInChronologicalOrder() {
        ProducerAward award1 = new ProducerAward("Producer1", 2010);
        ProducerAward award2 = new ProducerAward("Producer1", 2015);
        ProducerAward award3 = new ProducerAward("Producer1", 2020);
        awards.add(award1);
        awards.add(award2);
        awards.add(award3);
        when(producerGateway.findAllWinningProducersOrderByProducerAndMovieYear()).thenReturn(awards);

        AwardIntervalResult result = useCase.findProducerWithAwardIntervals();

        assertEquals(2, result.min().size());
        assertEquals(2, result.max().size());

        checkAwardInterval(award1, award2, result.min().get(0));
        checkAwardInterval(award2, award3, result.min().get(1));
        checkAwardInterval(award1, award2, result.max().get(0));
        checkAwardInterval(award2, award3, result.max().get(1));
    }

    @Test
    public void testMultipleAwardsWithDifferentInterval() {
        ProducerAward award1 = new ProducerAward("Producer1", 2010);
        ProducerAward award2 = new ProducerAward("Producer1", 2015);
        ProducerAward award3 = new ProducerAward("Producer2", 1900);
        ProducerAward award4 = new ProducerAward("Producer2", 1999);
        awards.add(award1);
        awards.add(award2);
        awards.add(award3);
        awards.add(award4);
        when(producerGateway.findAllWinningProducersOrderByProducerAndMovieYear()).thenReturn(awards);

        AwardIntervalResult result = useCase.findProducerWithAwardIntervals();

        assertEquals(1, result.min().size());
        assertEquals(1, result.max().size());

        checkAwardInterval(award1, award2, result.min().get(0));
        checkAwardInterval(award3, award4, result.max().get(0));
    }

    @Test
    public void testMultipleAwardsWithDifferentIntervalAndSameProducer() {
        ProducerAward award1 = new ProducerAward("Producer1", 1900);
        ProducerAward award2 = new ProducerAward("Producer1", 1999);
        ProducerAward award3 = new ProducerAward("Producer1", 2015);
        ProducerAward award4 = new ProducerAward("Producer1", 2022);
        awards.add(award1);
        awards.add(award2);
        awards.add(award3);
        awards.add(award4);
        when(producerGateway.findAllWinningProducersOrderByProducerAndMovieYear()).thenReturn(awards);

        AwardIntervalResult result = useCase.findProducerWithAwardIntervals();

        assertEquals(1, result.min().size());
        assertEquals(1, result.max().size());

        checkAwardInterval(award3, award4, result.min().get(0));
        checkAwardInterval(award1, award2, result.max().get(0));
    }

    @Test
    public void testMultipleAwardsWithDifferentIntervalAndDifferentProducer() {
        ProducerAward award1 = new ProducerAward("Producer1", 1900);
        ProducerAward award2 = new ProducerAward("Producer1", 1999); // interval 99 - max
        ProducerAward award3 = new ProducerAward("Producer1", 2015); // interval 15
        ProducerAward award4 = new ProducerAward("Producer1", 2022); // interval 7 - min

        ProducerAward award5 = new ProducerAward("Producer2", 1999);
        ProducerAward award6 = new ProducerAward("Producer2", 2015); // interval 16
        ProducerAward award7 = new ProducerAward("Producer2", 2022); // interval 7 - min

        awards.add(award1);
        awards.add(award2);
        awards.add(award3);
        awards.add(award4);
        awards.add(award5);
        awards.add(award6);
        awards.add(award7);
        when(producerGateway.findAllWinningProducersOrderByProducerAndMovieYear()).thenReturn(awards);

        AwardIntervalResult result = useCase.findProducerWithAwardIntervals();

        assertEquals(2, result.min().size());
        assertEquals(1, result.max().size());

        checkAwardInterval(award3, award4, result.min().get(0));
        checkAwardInterval(award6, award7, result.min().get(1));

        checkAwardInterval(award1, award2, result.max().get(0));
    }

    private void checkAwardInterval(ProducerAward award1, ProducerAward award2, AwardInterval awardInterval) {
        assertEquals(award1.getProducerName(), awardInterval.producer());
        assertEquals(award1.getMovieYear(), awardInterval.previousWin());
        assertEquals(award2.getMovieYear(), awardInterval.followingWin());
    }
}