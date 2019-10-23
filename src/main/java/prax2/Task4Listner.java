package prax2;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import prax2.config.Config;
import prax2.orderdao.Dao;
import prax2.orderdao.OrderDao;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;


@WebListener
public class Task4Listner implements ServletContextListener {

    private AnnotationConfigApplicationContext ctx;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ctx = new AnnotationConfigApplicationContext(Config.class);
        Dao.setDao(ctx.getBean(OrderDao.class));
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ctx.getBean(BasicDataSource.class).close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
/*
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

 */

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
