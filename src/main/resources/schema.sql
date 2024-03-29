drop table IF EXISTS mpa, director, films, users, film_director, reviews, review_likes, genres, film_genre, movie_likes, friendship, feed;


create table IF NOT EXISTS mpa
(
    mpa_id   int NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    mpa_name varchar(5),
    CONSTRAINT mpa_id_pk PRIMARY KEY (mpa_id)
);

create table IF NOT EXISTS director
(
    director_id   int          NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    director_name varchar(255) NOT NULL,
    CONSTRAINT director_id_pk PRIMARY KEY (director_id)
);

create table IF NOT EXISTS films
(
    film_id      bigint       NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    film_title   varchar(100) NOT NULL,
    description  varchar(200),
    release_date date         NOT NULL,
    duration     int          NOT NULL,
    mpa_id       int REFERENCES mpa (mpa_id),
    CONSTRAINT film_id_pk PRIMARY KEY (film_id)
);

create table IF NOT EXISTS users
(
    user_id   bigint       NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    user_name varchar(255),
    login     varchar(255) NOT NULL,
    email     varchar(255) NOT NULL,
    birthday  date         NOT NULL,
    CONSTRAINT user_id_pk PRIMARY KEY (user_id)
);

create table IF NOT EXISTS film_director
(
    film_id     bigint NOT NULL REFERENCES films (film_id) ON delete CASCADE,
    director_id int    NOT NULL REFERENCES director (director_id) ON delete CASCADE,
    CONSTRAINT film_director_pk PRIMARY KEY (film_id, director_id)
);

create table IF NOT EXISTS reviews
(
    reviews_id bigint        NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    user_id    bigint        NOT NULL REFERENCES users (user_id) ON delete CASCADE,
    film_id    bigint        NOT NULL REFERENCES films (film_id) ON delete CASCADE,
    content    varchar(1000) NOT NULL,
    isPositive boolean       NOT NULL,
    useful     bigint,
    CONSTRAINT reviews_id_pk PRIMARY KEY (reviews_id)
);

create table IF NOT EXISTS review_likes
(
    reviews_id bigint NOT NULL REFERENCES reviews (reviews_id) ON delete CASCADE,
    user_id    bigint NOT NULL REFERENCES users (user_id) ON delete CASCADE,
    isPositive boolean,
    CONSTRAINT review_likes_pk PRIMARY KEY (reviews_id, user_id)
);

create table IF NOT EXISTS genres
(
    genre_id   int         NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    genre_name varchar(50) NOT NULL,
    CONSTRAINT genre_id_pk PRIMARY KEY (genre_id)
);

create table IF NOT EXISTS film_genre
(
    film_id  bigint NOT NULL REFERENCES films (film_id) ON delete CASCADE,
    genre_id int    NOT NULL REFERENCES genres (genre_id),
    CONSTRAINT film_genre_pk PRIMARY KEY (film_id, genre_id)
);

create table IF NOT EXISTS movie_likes
(
    film_id bigint NOT NULL REFERENCES films (film_id) ON delete CASCADE,
    user_id bigint NOT NULL REFERENCES users (user_id) ON delete CASCADE,
    CONSTRAINT movie_likes_pk PRIMARY KEY (film_id, user_id)
);

create table IF NOT EXISTS friendship
(
    user_id_who_sent_request      bigint NOT NULL REFERENCES users (user_id) ON delete CASCADE,
    user_id_whom_request_was_sent bigint NOT NULL REFERENCES users (user_id) ON delete CASCADE,
    CONSTRAINT friendship_pk PRIMARY KEY (user_id_who_sent_request, user_id_whom_request_was_sent)
);

create table IF NOT EXISTS feed
(
    event_id   bigint      NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    time_stamp timestamp DEFAULT CURRENT_TIMESTAMP,
    user_id    bigint      NOT NULL REFERENCES users (user_id) ON delete CASCADE,
    operation  varchar(10) NOT NULL,
    event_type varchar(10) NOT NULL,
    entity_id  bigint      NOT NULL,
    CONSTRAINT event_id_pk PRIMARY KEY (event_id)
);