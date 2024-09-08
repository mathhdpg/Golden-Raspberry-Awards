package com.mg.gra.application.useCase;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mg.gra.application.parser.MovieDataParser;
import com.mg.gra.domain.gateways.MovieGateway;
import com.mg.gra.domain.gateways.ProducerGateway;
import com.mg.gra.domain.models.Movie;
import com.mg.gra.domain.models.Producer;

public class ImportMoviesFromCSVUseCase {

    private final MovieGateway movieGateway;
    private final ProducerGateway producerGateway;
    private final MovieDataParser movieDataParser;
    private final HashMap<String, Producer> producersMap = new HashMap<>();

    public ImportMoviesFromCSVUseCase(
            MovieGateway movieGateway,
            ProducerGateway producerGateway,
            MovieDataParser movieDataParser) {

        this.movieGateway = movieGateway;
        this.producerGateway = producerGateway;
        this.movieDataParser = movieDataParser;
    }

    public void importMoviesFromCSV(InputStream csvInputStream) {
        movieDataParser.parse(csvInputStream).forEach(movieData -> {
            if (movieGateway.existsByTitle(movieData.title())) {
                return;
            }

            List<Producer> producers = getProducersFromNames(movieData.producersNames());
            Movie movie = new Movie(movieData.title(), movieData.year(), movieData.winner(), producers);
            movieGateway.save(movie);
        });
    }

    private List<Producer> getProducersFromNames(String[] producersNames) {
        return Arrays.stream(producersNames)
                .map(name -> getOrCreateProducer(name))
                .toList();
    }

    private Producer getOrCreateProducer(String name) {
        if (producersMap.containsKey(name)) {
            return producersMap.get(name);
        }

        Producer producer = producerGateway.findByName(name)
                .orElseGet(() -> {
                    Producer newProducer = new Producer(name);
                    return producerGateway.save(newProducer);
                });

        producersMap.put(name, producer);
        return producer;
    }

}