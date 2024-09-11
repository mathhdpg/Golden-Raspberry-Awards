package com.mg.gra.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mg.gra.GraApplication;
import com.mg.gra.application.dto.MovieData;
import com.mg.gra.application.parser.MovieDataParser;
import com.mg.gra.application.useCase.ImportMoviesFromCSVUseCase;
import com.mg.gra.domain.gateways.MovieGateway;
import com.mg.gra.domain.gateways.ProducerGateway;
import com.mg.gra.domain.models.Movie;
import com.mg.gra.domain.models.Producer;

@ActiveProfiles("test")
@SpringBootTest(classes = GraApplication.class)
public class ImportMoviesFromCsvUseCaseUnitTest {

    @Mock
    private MovieGateway movieGateway;

    @Mock
    private ProducerGateway producerGateway;

    @Mock
    private MovieDataParser movieDataParser;

    private ImportMoviesFromCSVUseCase importMoviesFromCSVUseCase;

    @BeforeEach
    public void setup() {
        importMoviesFromCSVUseCase = new ImportMoviesFromCSVUseCase(
                movieGateway,
                producerGateway,
                movieDataParser);
    }

    @Test
    public void shouldImportMoviesFromCsv() throws Exception {
        List<MovieData> movieDataList = List.of(
                new MovieData(1980, "Can't Stop the Music", "Studio 1", new String[] { "Allan Carr" }, true),
                new MovieData(1980, "Cruising", "Studio 1", new String[] { "Jerry Weintraub" }, false));

        when(movieDataParser.parse(any(InputStream.class))).thenReturn(movieDataList);

        importMoviesFromCSVUseCase.importMoviesFromCSV(new ByteArrayInputStream(new byte[0]));

        verify(movieGateway, times(2)).save(any(Movie.class));
        verify(producerGateway, times(2)).save(any(Producer.class));
    }

    @Test
    public void shouldImportMoviesFromCsvWithMultipleProducersPerMovie() throws Exception {
        List<MovieData> movieDataList = List.of(
                new MovieData(1983, "Hercules", "Studio 1", new String[] { "Yoram Globus", "Menahem Golan" }, false),
                new MovieData(1986, "Under the Cherry Moon", "Studio 1",
                        new String[] { "Bob Cavallo", "Joe Ruffalo", "Steve Fargnoli" }, true));

        when(movieDataParser.parse(any(InputStream.class))).thenReturn(movieDataList);

        importMoviesFromCSVUseCase.importMoviesFromCSV(new ByteArrayInputStream(new byte[0]));

        verify(movieGateway, times(2)).save(any(Movie.class));
        verify(producerGateway, times(5)).save(any(Producer.class));
    }

    @Test
    public void shouldImportMoviesFromCsvWithSameProducerInMultipleMovies() throws Exception {
        List<MovieData> movieDataList = List.of(
                new MovieData(1983, "Hercules", "Studio 1",
                        new String[] { "Bob Cavallo", "Yoram Globus", "Menahem Golan" }, false),
                new MovieData(1986, "Under the Cherry Moon", "Studio 1",
                        new String[] { "Bob Cavallo", "Joe Ruffalo", "Steve Fargnoli" }, true));

        when(movieDataParser.parse(any(InputStream.class))).thenReturn(movieDataList);

        importMoviesFromCSVUseCase.importMoviesFromCSV(new ByteArrayInputStream(new byte[0]));

        verify(movieGateway, times(2)).save(any(Movie.class));
        verify(producerGateway, times(5)).save(any(Producer.class));
    }

    @Test
    public void shouldSaveNothingWhenImportMoviesFromCsvWithEmptyContent() throws Exception {
        when(movieDataParser.parse(any(InputStream.class))).thenReturn(List.of());

        importMoviesFromCSVUseCase.importMoviesFromCSV(new ByteArrayInputStream(new byte[0]));

        verify(movieGateway, times(0)).save(any(Movie.class));
        verify(producerGateway, times(0)).save(any(Producer.class));
    }

    @Test
    public void shouldThrowExceptionWhenImportMoviesFromCsvWithInvalidHeader() throws Exception {
        when(movieDataParser.parse(any(InputStream.class)))
                .thenThrow(new IllegalArgumentException("Invalid CSV header"));

        assertThrows(IllegalArgumentException.class, () -> {
            importMoviesFromCSVUseCase.importMoviesFromCSV(new ByteArrayInputStream(new byte[0]));
        });
    }

}