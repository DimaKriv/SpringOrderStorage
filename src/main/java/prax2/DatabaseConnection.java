package prax2;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class DatabaseConnection {
    private  BasicDataSource basicDataSource = null;

    public void createBasicDataSource(ConnectionInfo info, int maxConnection) {
        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl(info.getDbUrl());
        basicDataSource.setUsername(info.getDbUser());
        basicDataSource.setPassword(info.getDbPss());
        basicDataSource.setMaxTotal(maxConnection);
    }

    public Connection getConnection() throws SQLException {
        if (basicDataSource != null) {
            //System.out.println(basicDataSource.getNumActive() );
            return basicDataSource.getConnection();
        } return null;
    }

    public void close()  {
        try {
            basicDataSource.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

