alter table gra_movie add constraint unique_movie_year_title_studios unique (movie_year, title, studios)