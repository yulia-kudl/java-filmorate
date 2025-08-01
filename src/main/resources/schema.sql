CREATE TABLE IF NOT EXISTS Rate (
rate_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
rate VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Films (
film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR(255) NOT NULL,
description VARCHAR(255),
release_date DATE,
duration INTEGER NOT NULL,
rate_id INTEGER REFERENCES Rate (rate_id)
);


CREATE TABLE IF NOT EXISTS Genre (
genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Film_genre (
film_id INTEGER REFERENCES Films (film_id),
genre_id INTEGER REFERENCES Genre (genre_id)
);

CREATE TABLE IF NOT EXISTS Users (
user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email VARCHAR(255) NOT NULL,
login VARCHAR(255) NOT NULL,
name VARCHAR(255),
birthday DATE
);

CREATE TABLE IF NOT EXISTS Friendship (
request_user INTEGER REFERENCES Users (user_id),
to_user INTEGER REFERENCES Users (user_id)
);

CREATE TABLE IF NOT EXISTS Likes (
film_id INTEGER REFERENCES Films (film_id),
user_id INTEGER REFERENCES Users (user_id),
PRIMARY KEY(film_id, user_id)
);






