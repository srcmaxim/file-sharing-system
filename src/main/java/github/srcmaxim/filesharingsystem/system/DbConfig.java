package github.srcmaxim.filesharingsystem.system;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

import static github.srcmaxim.filesharingsystem.util.PropertyHolder.getVariableOrException;

@Configuration
@PropertySource("classpath:application.properties")
public class DbConfig {

    private Environment env;

    private String url;
    private String user;
    private String password;

    @Autowired
    public DbConfig(Environment env) {
        this.env = env;
        this.url = getVariableOrException(env, "DB_URL", "db.default-url");
        this.user = getVariableOrException(env,"DB_USER", "db.default-user");
        this.password = getVariableOrException(env,"DB_PWD", "db.default-password");
    }

    @Bean
    public DataSource dataSource() {
        PoolProperties p = getPoolProperties(url, user, password);
        org.apache.tomcat.jdbc.pool.DataSource datasource =
                new org.apache.tomcat.jdbc.pool.DataSource();
        datasource.setPoolProperties(p);
        return datasource;
    }

    private PoolProperties getPoolProperties(String url, String username, String password) {
        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName(env.getProperty("db.driver"));
        p.setUsername(username);
        p.setPassword(password);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(3 * 60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        return p;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Properties props = new Properties();
        props.setProperty("hibernate.format_sql", env.getProperty("db.format-sql"));
        props.setProperty("hibernate.hbm2ddl.auto", env.getProperty("db.generate-ddl"));
        props.setProperty("hibernate.hbm2ddl.import_files", env.getProperty("db.import-files"));

        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan(env.getProperty("db.model"));
        emf.setJpaVendorAdapter(jpaVendorAdapter());
        emf.setJpaProperties(props);

        return emf;
    }

}
