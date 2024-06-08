insert into student(name) values ('Maria Bueno');
insert into student(name) values ('João da Silva');

INSERT INTO users(username, "password", enabled)
VALUES('caio', '{noop}123456', true) on conflict do nothing;

INSERT INTO authorities(username, authority)
VALUES('caio', 'ROLE_USER') on conflict do nothing;