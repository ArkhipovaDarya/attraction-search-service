drop table if exists public.users;
drop table if exists public.roles;
drop table if exists public.users_role;

create table public.users (
    id bigserial not null,
    name varchar not null,
    email varchar not null unique,
    password varchar not null,
    primary key (id)
);

create table public.roles (
    id bigserial not null,
    name varchar not null,
    primary key (id)
);

create table public.users_roles (
    user_id integer not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);

insert into public.roles
values
    (1, 'ADMIN'),
    (2, 'USER');
