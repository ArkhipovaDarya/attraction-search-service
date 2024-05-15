drop table if exists public.attractions_tags;
drop table if exists public.attraction;
drop table if exists public.tag;

create table public.attraction (
    id bigserial not null,
    name varchar not null,
    category varchar not null,
    longitude float8 not null,
    latitude float8 not null,
    information varchar,
    rate float4 null,
    city_id bigint not null,
    primary key (id)
);

create table public.tag (
    id bigserial not null,
    name varchar not null unique,
    primary key (id)
);

create table public.attractions_tags (
    attraction_id integer not null,
    tag_id integer not null,
    primary key (attraction_id, tag_id)
);