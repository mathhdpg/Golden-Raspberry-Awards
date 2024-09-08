package com.mg.gra.application.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mg.gra.application.useCase.ImportMoviesFromCSVUseCase;

@Service
public class MovieImportService {

    private final ImportMoviesFromCSVUseCase importMoviesFromCSVUseCase;

    public MovieImportService(ImportMoviesFromCSVUseCase importMoviesFromCSVUseCase) {
        this.importMoviesFromCSVUseCase = importMoviesFromCSVUseCase;
    }

    @Transactional
    public void importMoviesFromCSV(InputStream csvInputStream) {
        importMoviesFromCSVUseCase.importMoviesFromCSV(csvInputStream);
    }

}
