create table client
(
    id         bigserial not null primary key,
    name       varchar(1500),
    address_id bigserial
);