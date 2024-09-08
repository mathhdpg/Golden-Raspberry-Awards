create sequence seq_gra_producer start with 1 increment by 1;

create table gra_producer(
    id int4 ,
    name varchar(255) not null,
    constraint pk_gra_producer primary key (id)
);