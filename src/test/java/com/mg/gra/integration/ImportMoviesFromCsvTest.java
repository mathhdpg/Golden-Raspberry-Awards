package com.mg.gra.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.mg.gra.GraApplication;
import com.mg.gra.application.useCase.ImportMoviesFromCSVUseCase;
import com.mg.gra.domain.gateways.MovieGateway;
import com.mg.gra.domain.models.Movie;

@ActiveProfiles("test")
@SpringBootTest(classes = GraApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ImportMoviesFromCsvTest {

    @Autowired
    private MovieGateway movieGateway;

    @Autowired
    private ImportMoviesFromCSVUseCase importMoviesFromCSVUseCase;

    @Test
    @Order(1)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldSaveImportedMoviesFromCsvInDatabase() throws Exception {
        String csvContent = "year;title;studios;producers;winner\n" +
                "1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes\n" +
                "1980;Cruising;Lorimar Productions, United Artists;Jerry Weintraub and Allan Carr;\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContent.getBytes());

        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
        List<Movie> movies = movieGateway.findAll();

        assertEquals(2, movies.size(), "Expected 2 movies to be saved in the database");

        Movie firstMovie = movies.get(0);
        verifyMovie(firstMovie, 1980, "Can't Stop the Music", "Associated Film Distribution", true);
        verifyProducerByMovieId(firstMovie, "Allan Carr");

        Movie secondMovie = movies.get(1);
        verifyMovie(secondMovie, 1980, "Cruising", "Lorimar Productions, United Artists", false);
        assertEquals(2, secondMovie.getProducers().size(), "Expected 2 producers for the movie 'Cruising'");
        verifyProducerByMovieId(secondMovie, "Jerry Weintraub", "Allan Carr");
    }

    @Test
    @Order(2)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldReturnErrorForInvalidCsvContent() throws Exception {
        String invalidCsvContent = "year;title;studios;producers;winner\n" +
                "invalidYear;Can't Stop the Music;;;\n";
        InputStream csvInputStream = new ByteArrayInputStream(invalidCsvContent.getBytes());

        assertThrows(IllegalArgumentException.class, () -> {
            importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
        }, "Expected IllegalArgumentException for invalid year format");
    }

    @Test
    @Order(3)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldNotDuplicateMoviesWithSameNameAndYearAndStudioOnImport() throws Exception {
        String csvContentWithDuplicates = "year;title;studios;producers;winner\n" +
                "1900;Movie 1;Studio 1;Producer 1;yes\n" +
                "1900;Movie 1;Studio 1;Producer 1;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithDuplicates.getBytes());

        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
        List<Movie> movies = movieGateway.findAll();

        assertEquals(1, movies.size(), "Expected only 1 movie after importing duplicates");
    }

    @Test
    @Order(4)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldImportMoviesWithoutWinners() throws Exception {
        String csvContentWithoutWinners = "year;title;studios;producers;winner\n" +
                "1980;Movie 1;Studio 1;Producer 1;\n" +
                "1981;Movie 2;Studio 2;Producer 2;\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithoutWinners.getBytes());

        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
        List<Movie> movies = movieGateway.findAll();

        assertEquals(2, movies.size(), "Expected 2 movies to be imported");
        assertFalse(movies.get(0).isWinner(), "Movie 1 should not be a winner");
        assertFalse(movies.get(1).isWinner(), "Movie 2 should not be a winner");
    }

    @Test
    @Order(5)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldImportMoviesWithMultipleProducers() throws Exception {
        String csvContent = "year;title;studios;producers;winner\n" +
                "1980;Movie with Multiple Producers;Studio A;Producer 1, Producer 2, Producer 3;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContent.getBytes());

        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
        List<Movie> movies = movieGateway.findAll();

        Movie movie = movies.get(0);
        assertEquals(3, movie.getProducers().size(), "Expected 3 producers for the movie");
        verifyProducerByMovieId(movie, "Producer 1", "Producer 2", "Producer 3");
    }

    @Test
    @Order(6)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldSaveMoviesWithSameNameAndStudioWithDifferentYear() throws Exception {
        String csvContentWithDuplicates = "year;title;studios;producers;winner\n" +
                "1900;Movie 1;Studio 1;Producer 1;yes\n" +
                "1901;Movie 1;Studio 1;Producer 1;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithDuplicates.getBytes());

        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
        List<Movie> movies = movieGateway.findAll();

        assertEquals(2, movies.size(), "Expected 2 movie after importing movies with same name and different year");
    }

    @Test
    @Order(7)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldSaveMoviesWithSameNameAndYearWithDifferentStudio() throws Exception {
        String csvContentWithDuplicates = "year;title;studios;producers;winner\n" +
                "1900;Movie 1;Studio 1;Producer 1;yes\n" +
                "1900;Movie 1;Studio 2;Producer 1;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithDuplicates.getBytes());

        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
        List<Movie> movies = movieGateway.findAll();

        assertEquals(2, movies.size(), "Expected 2 movie after importing movies with same name and different studio");
    }

    @Test
    @Order(8)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldSaveMoviesWithSameNameAndDifferentStudioAndYear() throws Exception {
        String csvContentWithDuplicates = "year;title;studios;producers;winner\n" +
                "1900;Movie 1;Studio 1;Producer 1;yes\n" +
                "1901;Movie 1;Studio 2;Producer 1;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithDuplicates.getBytes());

        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
        List<Movie> movies = movieGateway.findAll();

        assertEquals(2, movies.size(),
                "Expected 2 movie after importing movies with same name and different studio and year");
    }

    @Test
    @Order(8)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldThrowExceptionWhenImportMoviesWithoutStudio() throws Exception {
        String csvContentWithDuplicates = "year;title;studios;producers;winner\n" +
                "1900;Movie 1;;Producer 1;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithDuplicates.getBytes());

        assertThrows(IllegalArgumentException.class,
                () -> importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream),
                "Expected exception when saving movies without studios");
    }

    @Test
    @Order(9)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldThrowExceptionWhenImportMoviesWithoutName() throws Exception {
        String csvContentWithDuplicates = "year;title;studios;producers;winner\n" +
                "1900;;Studio 1;Producer 1;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithDuplicates.getBytes());
        assertThrows(IllegalArgumentException.class,
                () -> importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream),
                "Expected exception when saving movies without name");
    }

    @Test
    @Order(10)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldThrowExceptionWhenImportMoviesWithoutYear() throws Exception {
        String csvContentWithDuplicates = "year;title;studios;producers;winner\n" +
                ";Movie 1;Studio 1;Producer 1;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithDuplicates.getBytes());
        assertThrows(IllegalArgumentException.class,
                () -> importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream),
                "Expected exception when saving movies without year");
    }

    @Test
    @Order(11)
    @Transactional
    @Sql(scripts = { "/sql/common/clean_database.sql" })
    public void shouldThrowExceptionWhenImportMoviesWithoutProducers() throws Exception {
        String csvContentWithDuplicates = "year;title;studios;producers;winner\n" +
                "1900;Movie 1;Studio 1;;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContentWithDuplicates.getBytes());
        assertThrows(IllegalArgumentException.class,
                () -> importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream),
                "Expected exception when saving movies without producers");
    }

    private void verifyMovie(Movie movie, int expectedYear, String expectedTitle, String expectedStudios,
            boolean expectedWinnerStatus) {
        assertEquals(expectedYear, movie.getYear(), "Movie year is incorrect");
        assertEquals(expectedTitle, movie.getTitle(), "Movie title is incorrect");
        assertEquals(expectedStudios, movie.getStudios(), "Movie studios is incorrect");
        assertEquals(expectedWinnerStatus, movie.isWinner(), "Winner status is incorrect");
    }

    private void verifyProducerByMovieId(Movie movie, String... expectedProducers) {
        for (String expectedProducer : expectedProducers) {
            assertTrue(movie.getProducers().stream()
                    .anyMatch(producer -> producer.getName().equals(expectedProducer)),
                    "Producer " + expectedProducer + " not found in movie " + movie.getTitle());
        }
    }
}
