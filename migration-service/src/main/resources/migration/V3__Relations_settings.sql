ALTER TABLE orders ADD CONSTRAINT FK_orders_users
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE product_orders ADD CONSTRAINT FK_product_orders_orders
FOREIGN KEY (order_id) REFERENCES orders(id);

ALTER TABLE product_orders ADD CONSTRAINT FK_product_orders_product
FOREIGN KEY (product_id) REFERENCES product(id);

ALTER TABLE stock_product ADD CONSTRAINT FK_stock_product_stock
FOREIGN KEY (stock_id) REFERENCES stock(id);

ALTER TABLE stock_product ADD CONSTRAINT FK_stock_product_product
FOREIGN KEY (product_id) REFERENCES product(id);