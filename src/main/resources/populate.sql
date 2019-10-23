DELETE FROM order_task;
DELETE FROM order_row;

BEGIN TRANSACTION;
 INSERT INTO order_task(order_number) VALUES('Potate');
 INSERT INTO order_row(order_id,item_name, quantity, price) VALUES (currval('seq1'), 'Brown', 50, 5);
 INSERT INTO order_row(order_id,item_name, quantity, price) VALUES (currval('seq1'), 'White', 25, 10);
COMMIT;

BEGIN TRANSACTION;
INSERT INTO order_task(order_number) VALUES('Vegetables');
INSERT INTO order_row(order_id,item_name, quantity, price) VALUES (currval('seq1'), 'Carrot', 1, 2);
COMMIT;
