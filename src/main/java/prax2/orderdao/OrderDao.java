package prax2.orderdao;

import prax2.orderpojo.Order;
import prax2.orderpojo.OrderRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDao {

    private static OrderDao activeOrderDao = null;
    private DatabaseConnection activeConnection;
    private List<String> insertCommands = null;
    private List<String> selectAllCommands = null;
    private List<String> selectOneCommands = null;
    private List<String> insertAndReturnCommands = null;
    private List<String> deleteCommands = null;

    private OrderDao(DatabaseConnection connection) {
        activeConnection = connection;
    }

    public long saveOrderReturningId(Order order)  {
        if (insertCommands == null) {
            insertCommands = new SQLReader().readSQL("insert.sql");
        }
        try (Connection con = activeConnection.getConnection();
             PreparedStatement insertOrder = con.prepareStatement(insertCommands.get(0));
             PreparedStatement insertOrderRows = con.prepareStatement(insertCommands.get(1))) {
            return insertAndReturnId(insertOrder, insertOrderRows, order);
            } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

    public Order saveOrderAndReturnOrder(Order order) {
        if (insertAndReturnCommands == null) {
            insertAndReturnCommands = new SQLReader().readSQL("insertAndSelect.sql");
        }
        insertAndReturnCommands.stream().forEach(System.out::println);
        try ( Connection con = activeConnection.getConnection();
              PreparedStatement insertOrder = con.prepareStatement(insertAndReturnCommands.get(0));
              PreparedStatement insertRow = con.prepareStatement(insertAndReturnCommands.get(1));
              PreparedStatement getOrder = con.prepareStatement(insertAndReturnCommands.get(2))
              ){
          Long id = insertAndReturnId(insertOrder, insertRow, order);
          return  selectOrder(id, getOrder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  List<Order> getAllOrders() {
        if (selectAllCommands == null) {
            selectAllCommands = new SQLReader().readSQL("selectAll.sql");
        }
        try (Connection con = activeConnection.getConnection();
             Statement statment = con.createStatement()){
            ResultSet set = statment.executeQuery(selectAllCommands.get(0));
            List<Order> ans = new ArrayList<>();
            HashMap<Long, Order> orderMap = new HashMap<>();
            while(set.next()) {
                Long id = set.getLong("id");
                if (!orderMap.containsKey(id)) {
                    Order order = new Order(set.getLong("id")
                            , set.getString("order_number"), null);
                    orderMap.put(id, order);
                    ans.add(order);
                }
                Long rowId = set.getLong("order_id");
                if (!set.wasNull()) {
                    //System.out.println(set.wasNull() + " " + row_id);
                    OrderRow row = new OrderRow(rowId
                            , set.getString("item_name"), set.getInt("quantity")
                            , set.getInt("price"));
                    if (orderMap.get(rowId).getOrderRows() == null) {
                        orderMap.get(rowId)
                                .setOrderRows(new ArrayList<>(List.of(row)));
                    } else  {
                       // System.out.println(orderMap.get(row_id).getOrderRows());
                        orderMap.get(rowId).getOrderRows().add(row);
                    }
                }
            }
            return ans;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order getOrder(long id) {
        if (selectOneCommands == null) {
            selectOneCommands = new SQLReader().readSQL("selectOne.sql");
        }
        try (Connection con = activeConnection.getConnection();
                PreparedStatement statment = con.prepareStatement(selectOneCommands.get(0))) {
            return selectOrder(id, statment);
            } catch(SQLException e){
                throw new RuntimeException(e);
            }
        }

        private Order selectOrder(Long id, PreparedStatement statment) throws SQLException {
            statment.setLong(1, id);
            ResultSet set = statment.executeQuery();
            Order order = null;
            while (set.next()) {
                Long row = set.getLong("order_id");
                if (order == null) {
                    List<OrderRow> rowOrders = null;
                    if (!set.wasNull()) {
                        //System.out.println(set.wasNull());
                        rowOrders = new ArrayList<>();
                    }
                    order = new Order(set.getLong("id"), set.getString("order_number")
                            , rowOrders);
                    if (order.getOrderRows() == null) {
                        continue;
                    }
                    //System.out.println(row);
                }
                order.getOrderRows().add(new OrderRow( row, set.getString("item_name")
                        , set.getInt("quantity"), set.getInt("price")));
            }
            return order;
        }

        private Long insertAndReturnId(PreparedStatement insertOrder, PreparedStatement insertOrderRows
                , Order order) throws SQLException {
            insertOrder.setString(1, order.getOrderNumber());
            ResultSet resultSet = insertOrder.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
               // System.out.println(id);
                if (order.getOrderRows() != null) {
                    // System.out.println(order.getOrderRows());
                    for (OrderRow row : order.getOrderRows()) {
                        insertOrderRows.setLong(1, id);
                        insertOrderRows.setString(2, row.getItemName());
                        insertOrderRows.setInt(3, row.getQuantity());
                        insertOrderRows.setInt(4, row.getPrice());
                        insertOrderRows.addBatch();
                    }
                    insertOrderRows.executeBatch();
                }
                return id;
            } else {
                throw new SQLException("Id not generated");
            }
        }

    public void deleteOrder(long parseLong) {
        if (deleteCommands == null) {
            deleteCommands = new SQLReader().readSQL("delete.sql");
        }
        try (Connection con = activeConnection.getConnection();
             PreparedStatement deleteOrder = con.prepareStatement(deleteCommands.get(0));
             PreparedStatement deleteRow = con.prepareStatement(deleteCommands.get(1));
             ) {
            con.setAutoCommit(false);
            deleteOrder.setLong(1, parseLong);
            deleteRow.setLong(1, parseLong);
            deleteOrder.executeUpdate();
            deleteRow.executeUpdate();
            con.commit();
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static void setDAO(DatabaseConnection connection) {
        if (activeOrderDao == null) {
            activeOrderDao = new OrderDao(connection);
        } else {
            activeOrderDao.activeConnection = connection;
        }
    }

    public static OrderDao getDAO() {
        if (activeOrderDao != null && activeOrderDao.activeConnection != null) {
            return activeOrderDao;
        } return null;
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
