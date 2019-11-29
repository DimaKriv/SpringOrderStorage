package prax2.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"prax2.orderdao", "prax2.controller"})
public class Config {

    @Bean
    @Profile("!restart")
    public JdbcTemplate getTemplate(BasicDataSource source) {
        return new JdbcTemplate(source);
    }

    @Bean
    @Profile("restart")
    public JdbcTemplate getTemplateAndRestart(BasicDataSource source) {
        var populator = new ResourceDatabasePopulator(new ClassPathResource("populate.sql"));
        DatabasePopulatorUtils.execute(populator, source);
        return new JdbcTemplate(source);
    }

}
