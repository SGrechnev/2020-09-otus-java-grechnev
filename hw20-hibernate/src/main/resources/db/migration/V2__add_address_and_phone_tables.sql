create table address_data_set
(
    id     bigserial   not null primary key,
    street varchar(50) not null
);

create table phone_data_set
(
    id        bigserial   not null primary key,
    number    varchar(50) not null,
    client_id bigserial   not null
);