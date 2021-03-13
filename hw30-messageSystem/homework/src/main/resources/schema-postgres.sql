DROP TABLE IF EXISTS my_users;
CREATE TABLE my_users(
    login      varchar(50) not null primary key,
    name       varchar(50),
    password   varchar(50),
    role       int
);