package prax2.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@EnableWebMvc
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"prax2.orderdao", "prax2.controller"})
public class Config {

    @Bean
    public EntityManagerFactory getEntityManagerFactory(BasicDataSource dataSource
            , @Qualifier("dialect") String  dialect) {
        var populator = new ResourceDatabasePopulator(
                new ClassPathResource("schema.sql"));
        populator.addScript(new ClassPathResource("security_schema.sql"));
        DatabasePopulatorUtils.execute(populator, dataSource);
        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceProviderClass(
                HibernatePersistenceProvider.class);
        factory.setPackagesToScan("prax2.orderpojo");
        factory.setDataSource(dataSource);
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect",  dialect);
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "true");
        factory.setJpaProperties(properties);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean("dialect")
    public String getDialectHsql() {
        return "org.hibernate.dialect.HSQLDialect";
    }

    @Profile("psql")
    @Bean("dialect")
    public String getDialectPsql() {
        return "org.hibernate.dialect.HSQLDialect";
    }
}
