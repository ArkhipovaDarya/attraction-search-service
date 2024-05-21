drop table if exists public.city;

create table public.city (
    id bigserial not null,
    name varchar not null,
    primary key (id)
);

insert into public.city (name)
values
    ('city1'),
    ('city2');

