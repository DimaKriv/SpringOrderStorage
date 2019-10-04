package prax2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.postgresql.util.PGobject;

public class OrderDao {

    private static OrderDao activeOrderDao = null;
    private DatabaseConnection activeConnection;
    private List<String> insertCommands = null;
    private List<String> selectAllCommands = null;
    private List<String> selectOneCommands = null;
    private List<String> insertAndReturnCommands = null;

    private OrderDao(DatabaseConnection connection) {
        activeConnection = connection;
    }

    public long saveOrderReturningId(String orderJsonString)  {
        if (insertCommands == null) {
            insertCommands = new SQLReader().readSQL("insert.sql");
        }
        try (Connection con = activeConnection.getConnection();
             PreparedStatement prepereStatment = con.prepareStatement(insertCommands.get(0))){
                PGobject obj = new PGobject();
                obj.setType("jsonb");
                obj.setValue(orderJsonString);
                prepereStatment.setObject(1, obj);
                ResultSet resultSet = prepereStatment.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
                else{
                    throw new SQLException("Id not generated");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

    public  String saveOrderAndReturnOrder(String orderJsonString) {
        if (insertAndReturnCommands == null) {
            insertAndReturnCommands = new SQLReader().readSQL("insertAndSelect.sql");
        }
        try ( Connection con = activeConnection.getConnection();
              PreparedStatement prepereStatment = con.prepareStatement(insertAndReturnCommands.get(0))){
            PGobject obj = new PGobject();
            obj.setType("jsonb");
            obj.setValue(orderJsonString);
            prepereStatment.setObject(1, obj);
            ResultSet set =  prepereStatment.executeQuery();
           if (set.next()) {
               return addIdToJson(set.getLong(1), set.getString(2));
           } else {
               throw new RuntimeException("Not returned order");
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  List<String> getAllOrders() {
        if (selectAllCommands == null) {
            selectAllCommands = new SQLReader().readSQL("selectAll.sql");
        }
        try (Connection con = activeConnection.getConnection();
             Statement statment = con.createStatement()){
            ResultSet set = statment.executeQuery(selectAllCommands.get(0));
            List<String> ans = new ArrayList<>();
            while(set.next()) {
                String json = addIdToJson(set.getLong(1),set.getString(2));
                ans.add(json);
            }
            return ans;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  String getOrder(long id) {
        if (selectOneCommands == null) {
            selectOneCommands = new SQLReader().readSQL("selectOne.sql");
        }
        try (Connection con = activeConnection.getConnection();
                PreparedStatement statment = con.prepareStatement(selectOneCommands.get(0))) {
            statment.setLong(1, id);
            ResultSet set = statment.executeQuery();
            if (set.next()) {
                return addIdToJson(set.getLong(1),set.getString(2));
            } else {
                return null;
            }
            } catch(SQLException e){
                throw new RuntimeException(e);
            }
        }

    private String addIdToJson(long id, String json) {
        return  "{\"id\":" + id +", " + json.trim().substring(1);
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
