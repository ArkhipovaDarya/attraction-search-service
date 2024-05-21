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

insert into public.users
values
    (1,'daxaxa','xa@email.com','$2a$10$8Nw5pOTdgtJG/QC7lDCtx.btfMOSFzOju9swVUWjPkPiD4bDj1oGG'),
    (2,'diman','sdp10@email.com','$2a$10$dkU/0B7HcCL.UBbMJR2yPONS8eF46ScH7BxQymdYwYgPd16a9z28u'),
    (3,'julia','crazyfrog@email.com','$2a$10$2Gg/E/WBnrjVfW9KV6E5.umshKirzpyB4quBUhQqAgUnsxJ7CtEn2'),
    (4,'nik','helloworld@email.com','$2a$10$8jqGattVOy7kwjEDJDfAgOGpNkEbzSX51ayiIjHkQ4JYS0ijCsGdS'),
    (5,'kirill52','dota1love@email.com','$2a$10$5niTwxWKRFJ.wdUkspwMLe2ETk6x7HY3yQv9dQ7jgJDq/grH/efOe'),
    (6,'iiiiigor','geniy52@email.com','$2a$10$bTwl5e7ChkNO1RPvPlXOO.Gb/LSTvqmoGkErG7ugZv5K2K.1niAEC'),
    (7,'nastya','mirea-knv@email.com','$2a$10$GprcAAwOsDbjTs53tnztJuX2VknUrSfv1RmKuYuiC30hflu7MRrFy'),
    (8,'ksenia_m','slizerin@email.com','$2a$10$ilZCz6M3vNsa9KUHzcNQB.OyScEUO.SQeJbBVaBNvO7T/OSZyPxPa'),
    (9,'lerony19','openkids@email.com','$2a$10$MTncCTchwwjJE211NMruZ.VcsjLDgapYBLU4J8c618678of2EOTzS');

insert into public.users_roles
values (1,1),
       (2,2),
       (3,2),
       (4,2),
       (5,2),
       (6,2),
       (7,2),
       (8,2),
       (9,2);