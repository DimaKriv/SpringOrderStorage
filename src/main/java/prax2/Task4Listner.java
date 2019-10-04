package prax2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.util.Properties;
import java.util.stream.Collectors;

@WebListener
public class Task4Listner implements ServletContextListener {

    private DatabaseConnection databaseConnection = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        databaseConnection = new DatabaseConnection();
        Properties properties = readServerProperties();
        ConnectionInfo info= new ConnectionInfo(properties.getProperty("dbUrl")
                , properties.getProperty("dbUser"), properties.getProperty("dbPassword"));
        databaseConnection.createBasicDataSource(info, 2);
        OrderDao.setDAO(databaseConnection);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        databaseConnection.close();
    }

    private static Properties readServerProperties() {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("application.properties");
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String contest = buf.lines().collect(Collectors.joining("\n"));
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(contest));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

   /* public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Properties properties = readServerProperties();
        ConnectionInfo info= new ConnectionInfo(properties.getProperty("dbUrl")
                , properties.getProperty("dbUser"), properties.getProperty("dbPassword"));
        databaseConnection.createBasicDataSource(info, 2);
        OrderDao.setDAO(databaseConnection);
        System.out.println(OrderDao.getDAO().saveOrderAndReturnOrder("{\"prax\":1, \"line\":20}"));
    }*/
}
