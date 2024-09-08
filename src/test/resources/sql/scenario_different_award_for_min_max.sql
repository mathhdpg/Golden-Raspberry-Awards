INSERT INTO gra_producer (id, name) VALUES (1, 'Producer1');
INSERT INTO gra_producer (id, name) VALUES (2, 'Producer2');

INSERT INTO gra_movie (id, movie_year, title, winner) VALUES (1, 2000, 'Movie A', true);
INSERT INTO gra_movie (id, movie_year, title, winner) VALUES (2, 2001, 'Movie B', true);
INSERT INTO gra_movie_producer (movie_id, producer_id) VALUES (1, 1);
INSERT INTO gra_movie_producer (movie_id, producer_id) VALUES (2, 1);

INSERT INTO gra_movie (id, movie_year, title, winner) VALUES (3, 1990, 'Movie C', true);
INSERT INTO gra_movie (id, movie_year, title, winner) VALUES (4, 2020, 'Movie D', true);
INSERT INTO gra_movie_producer (movie_id, producer_id) VALUES (3, 2);
INSERT INTO gra_movie_producer (movie_id, producer_id) VALUES (4, 2);