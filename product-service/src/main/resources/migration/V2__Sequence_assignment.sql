CREATE SEQUENCE IF NOT EXISTS product_sequence START 101;

ALTER TABLE product ALTER COLUMN id SET DEFAULT nextval('product_sequence');