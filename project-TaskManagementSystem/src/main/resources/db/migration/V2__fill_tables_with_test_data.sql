----------------
-- Fill users --
----------------
insert into users
    (id,                             username,     fullname,        role)
values
    (nextval('hibernate_sequence'), 'admin',      'Administrator', 'ROLE_ADMIN'),
    (nextval('hibernate_sequence'), 'manager1',   'Manager 1',     'ROLE_MANAGER'),
    (nextval('hibernate_sequence'), 'manager2',   'Manager 2',     'ROLE_MANAGER'),
    (nextval('hibernate_sequence'), 'performer1', 'Performer 1',   'ROLE_PERFORMER'),
    (nextval('hibernate_sequence'), 'performer2', 'Performer 2',   'ROLE_PERFORMER'),
    (nextval('hibernate_sequence'), 'performer3', 'Performer 3',   'ROLE_PERFORMER');

----------------
-- Fill tasks --
----------------
insert into tasks
    (id,                            creator_id,                                       performer_id,                                       description, expected_result, expected_due_date, progress, actual_due_date)
values
    (nextval('hibernate_sequence'), (select id from users where username='manager1'), (select id from users where username='performer1'), 'm1 to p1',  'Result1',       '2021-04-15',      100,      null);

insert into tasks
    (id,                            creator_id,                                       performer_id,                                       description, expected_result, expected_due_date, progress, actual_due_date)
values
    (nextval('hibernate_sequence'), (select id from users where username='manager1'), (select id from users where username='performer1'), 'm1 to p1',  'Result2',       '2021-04-22',      0,        null);

insert into tasks
    (id,                            creator_id,                                       performer_id,                                       description, expected_result, expected_due_date, progress, actual_due_date)
values
    (nextval('hibernate_sequence'), (select id from users where username='manager1'), (select id from users where username='performer2'), 'm1 to p2',  'Result3',       '2021-04-29',      90,       null);

insert into tasks
    (id,                            creator_id,                                       performer_id,                                       description, expected_result, expected_due_date, progress, actual_due_date)
values
    (nextval('hibernate_sequence'), (select id from users where username='manager2'), (select id from users where username='performer2'), 'm2 to p2',  'Result4',       '2021-04-17',      20,       null);

insert into tasks
    (id,                            creator_id,                                       performer_id,                                       description, expected_result, expected_due_date, progress, actual_due_date)
values
    (nextval('hibernate_sequence'), (select id from users where username='manager2'), (select id from users where username='performer3'), 'm2 to p3',  'Result5',       '2021-04-24',      0,        '2021-04-21');

------------------
-- Fill reports --
------------------
insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result1'),  10,       'Report 1:1',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result1'),  20,       'Report 1:2',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result1'),  30,       'Report 1:3',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result1'),  40,       'Report 1:4',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result1'), 100,       'Report 1:5',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result3'),  10,       'Report 3:6',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result3'),  20,       'Report 3:7',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result3'),  90,       'Report 3:8',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result4'),  60,       'Report 4:9',  '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result4'),  20,       'Report 4:10', '2021-04-12');

insert into reports
    (id,                            task_id,                                              progress, comment,       creation_date)
values
    (nextval('hibernate_sequence'), (select id from tasks where expected_result='Result5'),   0,       'Report 5:11', '2021-04-12');