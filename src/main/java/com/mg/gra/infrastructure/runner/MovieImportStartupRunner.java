package com.mg.gra.infrastructure.runner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.mg.gra.application.service.MovieImportService;

@Component
@Profile("!test")
public class MovieImportStartupRunner implements CommandLineRunner {

    @Value("${movies.csv.path}")
    private String moviesCsvPath;

    private final MovieImportService movieImportService;

    private static final Logger logger = Logger.getLogger(MovieImportStartupRunner.class.getName());

    public MovieImportStartupRunner(MovieImportService movieImportService) {
        this.movieImportService = movieImportService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Iniciando importação de filmes...");
        try (InputStream csvInputStream = new FileInputStream(moviesCsvPath)) {
            movieImportService.importMoviesFromCSV(csvInputStream);
            logger.info("Filmes importados com sucesso!");
        } catch (Exception e) {
            logger.info("Erro ao importar filmes: " + e.getMessage());
        }
    }

}