CREATE TABLE users (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    username varchar(100) NOT NULL UNIQUE,
    name varchar(100) NOT NULL,
    age smallint NOT NULL CHECK(age > 0 and age < 100),
    email varchar NOT NULL UNIQUE,
    phone_number varchar(11) NOT NULL UNIQUE,
    created_at timestamp
);

