create sequence hibernate_sequence start with 1 increment by 1;

create table users(
    id          bigserial,
    username    varchar(50) not null unique,
    fullname    varchar(50),
    role        varchar(50)
);

create table tasks(
    id                  bigserial,
    performer_id        bigint not null,
    creator_id          bigint not null,
    description         varchar(500) not null,
    expected_result     varchar(250) not null,
    expected_due_date   date not null,
    actual_due_date     date,
    progress            smallint
);

create table reports(
    id              bigint not null primary key,
    task_id         bigint not null,
    progress        smallint not null,
    comment         varchar(500),
    creation_date   date
);