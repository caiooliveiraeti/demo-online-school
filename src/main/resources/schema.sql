create table if not exists student(
  id serial PRIMARY KEY,
  name varchar(100) not null
);

-- Spring Security
create table if not exists users(
    username varchar(50) not null primary key,
    password varchar(500) not null,
    enabled boolean not null
);
create table if not exists authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index if not exists username_authority_key on authorities (username, authority);