drop table if exists public.review;

create table public.review (
    id bigserial not null,
    rate integer not null,
    text varchar,
    attraction_id integer not null,
    user_id integer not null,
    primary key (id)
);