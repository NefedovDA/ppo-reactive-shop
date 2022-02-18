create type currency_enum as
    enum ('RUBLE', 'DOLLAR', 'EURO');

create type user_role_enum as
    enum ('USER', 'ADMIN', 'OWNER');

create type full_name_type as
(
    first_name  varchar(256),
    last_name   varchar(256),
    author_name varchar(256)
);

create table products
(
    product_id  bigint        not null
        primary key,

    title       varchar(256)  not null,
    description varchar(1024) not null,

    amount      integer       not null,

    price       integer       not null,

    check ( amount >= 0 )
);

create sequence product_ids start 1 increment 1;


create table users
(
    user_id           bigint         not null
        primary key,

    full_name         full_name_type not null,

    selected_currency currency_enum  not null,

    email             varchar(256)   not null
        unique,
    password_hash     varchar(256)   not null,
    user_role         user_role_enum not null
);

create sequence user_ids start 1 increment 1;


create function do_register_user(
    p_full_name full_name_type,
    p_selected_currency currency_enum,
    p_email varchar(256),
    p_password_hash varchar(256)
)
    returns bigint
as
$$
declare
    v_user_id bigint;

begin
    v_user_id = nextval('user_ids');

    insert into users (user_id, full_name, selected_currency, email, password_hash, user_role)
    values (v_user_id, p_full_name, p_selected_currency, p_email, p_password_hash, 'USER');

    return v_user_id;

exception
    when others then
        return null;
end;
$$
    language plpgsql
    security definer;


create function do_add_product(
    p_title varchar(256),
    p_description varchar(1024),
    p_amount integer,
    p_price integer
)
    returns bigint
as
$$
declare
    v_product_id bigint;

begin
    v_product_id = nextval('product_ids');

    insert into products (product_id, title, description, amount, price)
    values (v_product_id, p_title, p_description, p_amount, p_price);

    return v_product_id;

exception
    when others then
        return null;
end;
$$
    language plpgsql
    security definer;


create function do_update_product_amount(
    p_product_id bigint,
    p_diff integer
)
    returns boolean
as
$$
begin
    update products
    set amount = amount + p_diff
    where product_id = p_product_id;

    return true;

exception
    when others then
        return false;
end;
$$
    language plpgsql
    security definer;


create function do_change_role(
    p_user_id bigint,
    p_new_role user_role_enum
)
    returns boolean
as
$$
begin
    update users
    set user_role = p_new_role
    where user_id = p_user_id;

    return true;
exception
    when others then
        return false;
end;
$$
