package prax2.orderdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import prax2.orderpojo.Order;
import prax2.orderpojo.OrderRow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class OrderDao {

    private JdbcTemplate template;
    //private List<String> insertCommands = null;
    private List<String> selectAllCommands = null;
    private List<String> selectOneCommands = null;
   // private List<String> insertAndReturnCommands = null;
    private List<String> deleteCommands = null;

    @Autowired
    public OrderDao(JdbcTemplate template) {
        this.template = template;
    }

    public long saveOrderReturningId(Order order)  {
        var orderNumber = Map.of("order_number", order.getOrderNumber());
        Number id =  new SimpleJdbcInsert(template).withTableName("order_task")
                .usingGeneratedKeyColumns("id").executeAndReturnKey(orderNumber);
        List<Map<String, Object>> batchValues = new ArrayList<>(order.getOrderRows().size());
        for (OrderRow row : order.getOrderRows()) {
            Map<String, Object> map = new HashMap<>();
            map.put("item_name", row.getItemName());
            map.put("quantity", row.getQuantity());
            map.put("price", row.getPrice());
            map.put("order_id", id.longValue());
            batchValues.add(map);
        }
            new SimpleJdbcInsert(template).withTableName("order_row").executeBatch(
                    batchValues.toArray(new Map[batchValues.size()]));
        return   id.longValue();
    }

    public Order saveOrderAndReturnOrder(Order order) {
        Long id = saveOrderReturningId(order);
          order.setId(id);
          return order;
    }

    public  List<Order> getAllOrders() {
        if (selectAllCommands == null) {
            selectAllCommands = new SQLReader().readSQL("selectAll.sql");
        }
        OrderMapper mapper = new OrderMapper();
        template.query(selectAllCommands.get(0), mapper);
        return mapper.getOrders();
    }

    public Order getOrder(long id) {
        if (selectOneCommands == null) {
            selectOneCommands = new SQLReader().readSQL("selectOne.sql");
        }
        OrderMapper mapper = new OrderMapper();
        template.query(selectOneCommands.get(0), new Object[]{id}, mapper);
        return mapper.getOrders().get(0);
        }

    private class OrderMapper implements RowMapper<Boolean> {
        private Map<Long, Integer> orderIds = new HashMap<>();
        private List<Order> orders = new ArrayList<>();
        public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
         OrderRow row = null;
         Long id = rs.getLong("id");
         Long orderId = rs.getLong("order_id");
         if (!rs.wasNull()) {
             row = new OrderRow();
             row.setItemName(rs.getString("item_name"));
             row.setQuantity(rs.getInt("quantity"));
             row.setPrice(rs.getInt("price"));
             row.setOrderId(orderId);
         }
         if (orderIds.containsKey(id)) {
             if (row != null) {
                 orders.get(orderIds.get(id)).getOrderRows().add(row);
             }
         } else {
             List<OrderRow> rows = null;
             if (row != null) {
                 rows = new ArrayList<>(List.of(row));
             }
             Order order = new Order(id, rs.getString("order_number"), rows);
             orderIds.put(id, orders.size());
             orders.add(order);
         }
         return false;
        }

        public List<Order> getOrders() {
            return orders;
        }
    }

    public void deleteOrder(long parseLong) {
        if (deleteCommands == null) {
            deleteCommands = new SQLReader().readSQL("delete.sql");
        }
        template.execute(deleteCommands.get(0));
        template.execute(deleteCommands.get(1));
    }

    public SQLReader test() {
        return new SQLReader();
    }

    private class SQLReader {
        //private final String resource = "src/main/resources/";

        public List<String> readSQL(String fileName) {
           // System.out.println(Thread.currentThread().getContextClassLoader().);
            try (InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream( fileName)) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
                return parseSQLToJAVASQL(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private List<String> parseSQLToJAVASQL(BufferedReader buffer) {
            String[] lines = buffer.lines().collect(Collectors.joining("\t"))
                    .split(";");
            return Arrays.stream(lines).map(String::trim).collect(Collectors.toList());
        }
    }

 /*   public static void main(String[] args) {
        OrderDao.setDAO(new DatabaseConnection());
        SQLReader test = OrderDao.getDAO().test();
        List<String> text = test.readSQL("insert.sql");
        text.forEach(System.out::println);
    }*/
}
