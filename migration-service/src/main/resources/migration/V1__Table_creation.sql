CREATE TABLE IF NOT EXISTS product(
    id BIGINT PRIMARY KEY,
    name VARCHAR(300) NOT NULL UNIQUE ,
    description VARCHAR(600) NOT NULL,
    price INTEGER NOT NULL CHECK (price > 0)
);

CREATE TABLE IF NOT EXISTS users(
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE ,
    email VARCHAR(50) NOT NULL UNIQUE CHECK ( email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' ) ,
    password VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL UNIQUE CHECK ( phone ~* '^\+\d{12}$' )
);

CREATE TABLE IF NOT EXISTS roles(
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders(
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_number BIGINT NOT NULL UNIQUE,
    order_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS product_order(
    product_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS stock(
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS stock_product(
    stock_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL
);
