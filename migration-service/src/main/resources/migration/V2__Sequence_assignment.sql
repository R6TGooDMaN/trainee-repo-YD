CREATE SEQUENCE IF NOT EXISTS product_sequence START 101;

CREATE SEQUENCE IF NOT EXISTS orders_sequence START 101;
ALTER TABLE orders ALTER COLUMN id SET DEFAULT nextval('orders_sequence');

CREATE SEQUENCE IF NOT EXISTS users_sequence START 101;
ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_sequence');

CREATE SEQUENCE IF NOT EXISTS stock_sequence START 101;
ALTER TABLE stock ALTER COLUMN id SET DEFAULT nextval('stock_sequence');




