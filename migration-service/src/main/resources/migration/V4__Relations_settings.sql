ALTER TABLE orders ADD CONSTRAINT FK_orders_users
FOREIGN KEY (user_id) REFERENCES users(id);