DROP TABLE IF EXISTS order_rows;
DROP TABLE IF EXISTS orders;
DROP SEQUENCE IF EXISTS seq1;

CREATE SEQUENCE seq1 AS INTEGER START WITH 1;

CREATE TABLE order_task (
                        id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('seq1'),
                        order_number VARCHAR(255)
);

CREATE TABLE order_row (
                            item_name VARCHAR(255),
                            price INT,
                            quantity INT,
                            orders_id BIGINT,
                            FOREIGN KEY (orders_id)
                                REFERENCES order_task ON DELETE CASCADE
);

INSERT INTO order_task(order_number) VALUES ('test');