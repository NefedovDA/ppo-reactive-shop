create type authority.users_roles as enum ('OWNER', 'ADMIN', 'USER');

create table authority.users
(
    user_id  bigserial    not null
        primary key,

    login    varchar(256) not null
        unique,
    password varchar(256) not null,

    role     varchar(10)  not null,

    check ( role::authority.users_roles = any (enum_range(null::authority.users_roles)) )
);