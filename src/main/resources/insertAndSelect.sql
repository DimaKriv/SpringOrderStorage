INSERT INTO order_task(order_number) VALUES(?) returning id;
INSERT INTO order_row(order_id, item_name, quantity, price) VALUES(?,?,?,?);
SELECT * FROM order_task LEFT JOIN order_row o on order_task.id = o.order_id WHERE id=?;