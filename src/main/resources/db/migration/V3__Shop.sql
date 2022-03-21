create type shop.currency as enum ('RUB', 'EUR', 'USD');

create table shop.products
(
    product_id     bigserial     not null
        primary key,

    title          varchar(128)  not null,
    description    varchar(1024) not null,

    price_amount   bigint        not null,
    price_currency varchar(10)   not null,

    user_id        bigint        not null
        references authority.users (user_id),

    check ( price_amount >= 0 ),
    check ( price_currency::shop.currency = any (enum_range(null::shop.currency)) )
);
