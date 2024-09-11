package com.mg.gra.infrastructure.parser;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import com.mg.gra.application.dto.MovieData;
import com.mg.gra.application.parser.MovieDataParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MovieDataParserImpl implements MovieDataParser {

    private final String HEADER_COLUMNS = "year;title;studios;producers;winner";

    @Override
    public List<MovieData> parse(final InputStream inputStream) {
        final List<MovieData> movies = new ArrayList<>();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            final String header = reader.readLine();
            if (header == null || header.isBlank()) {
                return movies;
            }

            validateHeader(header);

            String line;
            while ((line = reader.readLine()) != null) {
                final String[] columns = line.split(";");

                validateColumnsSize(columns);

                final int year = Integer.parseInt(columns[0]);
                final String title = columns[1];
                final String studios = columns[2];
                final String producersString = columns[3];
                final boolean isWinner = columns.length >= 5 && "yes".equalsIgnoreCase(columns[4]);

                String[] producersNames = producersString.split("(,\\s*|\\s+and\\s+)");

                movies.add(new MovieData(year, title, studios, producersNames, isWinner));
            }

            return movies;
        } catch (final IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    private void validateColumnsSize(String[] columns) {
        if (columns.length < 4) {
            throw new IllegalArgumentException("Invalid CSV line");
        }
    }

    private void validateHeader(String header) {
        if (!header.toLowerCase().startsWith(HEADER_COLUMNS)) {
            throw new IllegalArgumentException("Invalid CSV header");
        }
    }
}