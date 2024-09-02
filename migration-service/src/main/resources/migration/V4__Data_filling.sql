INSERT INTO product(name,description,price)
VALUES ('Phone','Good phone', 2000),
       ('Display','Good display', 1500),
       ('Web-camera','Good camera', 3000);

INSERT INTO users(username,email,password,phone,roles)
VALUES ('Dima','super.email322@java.com','Goodparol123','+375447654321','ADMIN'),
       ('Sasha','maxmail123@google.com','Mediumparol123','+375441234567','USER'),
       ('Anton','asdedx542@yandex.ru','+Hardparol123','+375291234567','USER');

INSERT INTO orders(user_id,order_number,    order_date,order_status)
VALUES (126,41341,'1999-01-08','CLOSED'),
       (151,52412,'1999-01-08','CANCELED'),
       (126,12432,'1999-01-08','OPENED'),
       (151,64351,'1999-01-08','CANCELED'),
       (126,1421,'1999-01-08','CLOSED'),
       (151,9854,'1999-01-08','OPENED');

INSERT INTO product_order(product_id, order_id, quantity)
VALUES (101,201,3),
       (126,176,5),
       (151,151,1),
       (101,101,2),
       (126,201,1),
       (151,226,3),
       (101,101,5);

INSERT INTO stock(name)
VALUES ('Gomel stock'),
       ('SuperSklad stock'),
       ('Brest stock');

INSERT INTO stock_product(stock_id, product_id, quantity)
VALUES (101,101,500),
       (126,101,300),
       (151,126,400),
       (101,126,350),
       (126,151,150);