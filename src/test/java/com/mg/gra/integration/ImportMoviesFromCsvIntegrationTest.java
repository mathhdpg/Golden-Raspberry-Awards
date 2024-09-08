package com.mg.gra.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.mg.gra.GraApplication;
import com.mg.gra.application.useCase.ImportMoviesFromCSVUseCase;
import com.mg.gra.domain.gateways.MovieGateway;
import com.mg.gra.domain.gateways.ProducerGateway;
import com.mg.gra.domain.models.Movie;
import com.mg.gra.domain.models.Producer;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(classes = GraApplication.class)
public class ImportMoviesFromCsvIntegrationTest {

    @Autowired
    private MovieGateway movieGateway;

    @Autowired
    private ProducerGateway producerGateway;

    @Autowired
    private ImportMoviesFromCSVUseCase importMoviesFromCSVUseCase;

    @Test
    public void shouldSaveImportedMoviesFromCsvInDatabase() throws Exception {
        String csvContent = "year;title;studios;producers;winner\n" +
                "1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes\n" +
                "1980;Cruising;Lorimar Productions, United Artists;Jerry Weintraub and Allan Carr;\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContent.getBytes());

        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);

        // Verifique se os filmes foram salvos corretamente
        assertEquals(2, movieGateway.findAll().size());

        // Verifique o primeiro filme
        Movie movie = movieGateway.findById(1).orElseThrow();
        assertEquals(1980, movie.getYear());
        assertEquals("Can't Stop the Music", movie.getTitle());
        assertTrue(movie.isWinner());

        // Verifique o produtor do filme
        Producer producer = producerGateway.findById(1).orElseThrow();
        assertEquals("Allan Carr", producer.getName());

        // Verifique o segundo filme
        movie = movieGateway.findById(2).orElseThrow();
        assertEquals(1980, movie.getYear());
        assertEquals("Cruising", movie.getTitle());
        assertFalse(movie.isWinner());

        assertEquals(movie.getProducers().size(), 2);

        producer = producerGateway.findById(2).orElseThrow();
        assertEquals("Jerry Weintraub", producer.getName());
    }
}
