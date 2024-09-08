INSERT INTO gra_producer (id, name) VALUES (1, 'Producer1');
INSERT INTO gra_movie (id, movie_year, title, winner) VALUES (1, 2000, 'Movie A', true);
INSERT INTO gra_movie (id, movie_year, title, winner) VALUES (2, 2010, 'Movie B', true);
INSERT INTO gra_movie_producer (movie_id, producer_id) VALUES (1, 1);
INSERT INTO gra_movie_producer (movie_id, producer_id) VALUES (2, 1);