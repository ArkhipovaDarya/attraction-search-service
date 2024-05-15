drop table if exists public.verification_token;

create table public.verification_token (
    id bigserial not null,
    token varchar(255),
    user_id bigint not null,
    primary key (id)
)