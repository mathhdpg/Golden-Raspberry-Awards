create sequence seq_gra_movie start with 1 increment by 1;

create table gra_movie (
    id int4 not null default nextval('seq_gra_movie'),
    title varchar(255) not null,
    movie_year integer not null,
    winner boolean not null,
    constraint pk_gra_movie primary key (id)
);

create table gra_movie_producer (
    movie_id int4 not null,
    producer_id int4 not null,
    constraint pk_gra_movie_producer primary key (movie_id, producer_id),
    constraint fk_gra_movie_producer_movie foreign key (movie_id) references gra_movie(id),
    constraint fk_gra_movie_producer_producer foreign key (producer_id) references gra_producer(id)
);