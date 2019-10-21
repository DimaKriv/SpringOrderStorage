INSERT INTO order_task(order_number) VALUES(?) returning id;
INSERT INTO order_row(order_id, item_name, quantity, price) VALUES(?);