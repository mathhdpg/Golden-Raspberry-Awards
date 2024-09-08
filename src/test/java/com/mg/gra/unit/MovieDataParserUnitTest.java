package com.mg.gra.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.mg.gra.application.dto.MovieData;
import com.mg.gra.application.parser.MovieDataParser;
import com.mg.gra.infrastructure.parser.MovieDataParserImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@ActiveProfiles("test")
public class MovieDataParserUnitTest {

    private MovieDataParser movieDataParser;

    @BeforeEach
    public void setup() {
        movieDataParser = new MovieDataParserImpl();
    }

    @Test
    public void shouldReadMoviesFromCsv() throws Exception {
        String csvContent = "year;title;studios;producers;winner\n" +
                "1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes\n" +
                "1980;Cruising;Lorimar Productions, United Artists;Jerry Weintraub;\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContent.getBytes());
        List<MovieData> movies = movieDataParser.parse(csvInputStream);

        assertEquals(2, movies.size());

        assertEquals(1980, movies.get(0).year());
        assertEquals("Can't Stop the Music", movies.get(0).title());
        assertEquals("Allan Carr", movies.get(0).producersNames()[0]);

        assertTrue(movies.get(0).winner());

        assertEquals(1980, movies.get(1).year());
        assertEquals("Cruising", movies.get(1).title());
        assertEquals("Jerry Weintraub", movies.get(1).producersNames()[0]);
        assertFalse(movies.get(1).winner());
    }

    @Test
    public void shouldReadMoviesFromCsvWithMultipleProducers() throws Exception {
        String csvContent = "year;title;studios;producers;winner\n" +
                "1986;Under the Cherry Moon;Warner Bros.;Bob Cavallo, Joe Ruffalo and Steve Fargnoli;yes\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvContent.getBytes());
        List<MovieData> movies = movieDataParser.parse(csvInputStream);

        assertEquals(1, movies.size());

        assertEquals("Bob Cavallo", movies.get(0).producersNames()[0]);
        assertEquals("Joe Ruffalo", movies.get(0).producersNames()[1]);
        assertEquals("Steve Fargnoli", movies.get(0).producersNames()[2]);
    }

    @Test
    public void shouldReturnEmptyListWhenReadMoviesFromEmptyCsv() throws Exception {
        String csvContent = "";
        InputStream csvInputStream = new ByteArrayInputStream(csvContent.getBytes());

        List<MovieData> movies = movieDataParser.parse(csvInputStream);
        assertEquals(0, movies.size());
    }

    @Test
    public void shouldThrowExceptionWhenReadMoviesFromInvalidHeaderCsv() throws Exception {
        String csvContent = "invalid_column;title;studios;producers;winner";
        InputStream csvInputStream = new ByteArrayInputStream(csvContent.getBytes());

        assertThrows(IllegalArgumentException.class, () -> {
            movieDataParser.parse(csvInputStream);
        });
    }

}
