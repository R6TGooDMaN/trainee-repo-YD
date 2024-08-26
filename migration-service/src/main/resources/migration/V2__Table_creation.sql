CREATE TABLE IF NOT EXISTS product(
    id BIGINT PRIMARY KEY DEFAULT nextval('product_sequence'),
    name VARCHAR(300) NOT NULL UNIQUE ,
    description VARCHAR(600) NOT NULL,
    price INTEGER NOT NULL CHECK (price > 0)
);

CREATE TABLE IF NOT EXISTS users(
    id BIGINT PRIMARY KEY DEFAULT nextval('users_sequence'),
    username VARCHAR(50) NOT NULL UNIQUE ,
    email VARCHAR(50) NOT NULL UNIQUE CHECK ( email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' ) ,
    password VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL UNIQUE CHECK ( phone ~* '^\+\d{12}$' ),
    roles VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles(
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders(
    id BIGINT PRIMARY KEY DEFAULT nextval('orders_sequence'),
    user_id BIGINT NOT NULL,
    order_number BIGINT NOT NULL UNIQUE,
    order_date DATE NOT NULL,
    order_status VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS product_order(
    product_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS stock(
    id BIGINT PRIMARY KEY DEFAULT nextval('stock_sequence'),
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS stock_product(
    stock_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL
);
