package prax2.orderdao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/application.properties")
public class DatabaseConnection {

    @Autowired
    public Environment env;

    private static final int MAX_CONNECTION_POOL = 2;

    @Bean
    public BasicDataSource createBasicDataSource() {
        ConnectionInfo info = new ConnectionInfo(env.getProperty("dbUrl")
                , env.getProperty("dbUser"), env.getProperty("dbPassword"));
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl(info.getDbUrl());
        basicDataSource.setUsername(info.getDbUser());
        basicDataSource.setPassword(info.getDbPss());
        basicDataSource.setMaxTotal(MAX_CONNECTION_POOL);
        return basicDataSource;
    }


}

