create table files
(
    id   serial primary key,
    name varchar not null,
    path varchar not null unique
);

comment on table files is 'File table';
comment on column files.id is 'File id';
comment on column files.name is 'File name';
comment on column files.path is 'File path';

create table genres
(
    id   serial primary key,
    name varchar unique not null
);

comment on table genres is 'Genre table';
comment on column genres.id is 'Genre id';
comment on column genres.name is 'Genre name';

create table films
(
    id                  serial primary key,
    name                varchar                    not null,
    description         varchar                    not null,
    "year"              int                        not null,
    genre_id            int references genres (id) not null,
    minimal_age         int                        not null,
    duration_in_minutes int                        not null,
    file_id             int references files (id)  not null
);

comment on table films is 'Film table';
comment on column films.id is 'Film id';
comment on column films.name is 'Film name';
comment on column films.description is 'Film description';
comment on column films."year" is 'Film laucnh year';
comment on column films.genre_id is 'Film genre';
comment on column films.minimal_age is 'Minimal access age';
comment on column films.duration_in_minutes is 'Film duration';
comment on column films.file_id is 'Film poster';


create table halls
(
    id          serial primary key,
    name        varchar not null,
    row_count   int     not null,
    place_count int     not null,
    description varchar not null
);

comment on table halls is 'Hall table';
comment on column halls.id is 'Hall id';
comment on column halls.name is 'Hall name';
comment on column halls.row_count is 'Hall rows';
comment on column halls.place_count is 'Hall places in each row';
comment on column halls.description is 'Hall description';

create table film_sessions
(
    id         serial primary key,
    film_id    int references films (id) not null,
    hall_id   int references halls (id) not null,
    start_time timestamp                 not null,
    end_time   timestamp                 not null,
    price      int                       not null
);

comment on table film_sessions is 'Film Session table';
comment on column film_sessions.id is 'Session id';
comment on column film_sessions.film_id is 'Session film';
comment on column film_sessions.hall_id is 'Session hall';
comment on column film_sessions.start_time is 'Session start time';
comment on column film_sessions.end_time is 'Session end time';
comment on column film_sessions.price is 'Session ticket price';

create table users
(
    id        serial primary key,
    name     varchar        not null,
    email     varchar unique not null,
    password  varchar        not null
);

comment on table users is 'User table';
comment on column users.id is 'User id';
comment on column users.name is 'User name';
comment on column users.email is 'User email';
comment on column users.password is 'User password';

create table tickets
(
    id           serial primary key,
    session_id   int references film_sessions (id) not null,
    row_number   int                               not null,
    place_number int                               not null,
    user_id      int                               not null,
    unique (session_id, row_number, place_number)
);

comment on table tickets is 'Ticket table';
comment on column tickets.id is 'Ticket id';
comment on column tickets.session_id is 'Film session';
comment on column tickets.row_number is 'Row number';
comment on column tickets.place_number is 'Place number';
comment on column tickets.user_id is 'User, bought this ticket';