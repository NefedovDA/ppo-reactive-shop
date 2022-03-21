create table shop.users_configs
(
    user_config_id   bigserial   not null
        primary key,

    user_id          bigint      not null
        unique
        references authority.users (user_id),

    default_currency varchar(10) not null,

    check ( default_currency::shop.currency = any (enum_range(null::shop.currency)) )
);