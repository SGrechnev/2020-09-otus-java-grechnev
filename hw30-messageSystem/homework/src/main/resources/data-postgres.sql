insert into my_users
    (login,       name,          password,  role)
values
    ('johndoe',   'John Doe',    'johndoe',   1), -- 1 = ADMIN
    ('johnwick',  'John Wick',   'johnwick',  0), -- 0 = USER
    ('johnsmith', 'John Smith',  'johnsmith', 1),
    ('ivanovii',  'Ivanov Ivan', 'ivanovii',  0),
    ('admin',     'No Name',     'hardpswd',  1),
    ('user1',     'User Meme',   'usermeme',  1),
    ('<s>super',  '<S>Superman', '<s>super',  0);