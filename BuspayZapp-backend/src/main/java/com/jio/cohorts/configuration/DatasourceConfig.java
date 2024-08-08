package com.jio.cohorts.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

@Configuration
public class DatasourceConfig {

    @Autowired
    PasswordDecryptor passwordDecryptor;

    @Bean
    public ComboPooledDataSource getDatasource(@Value("${mysql.datasource.username}") String username,
                                                                   @Value("${mysql.datasource.password}") String password,
                                                                   @Value("${mysql.datasource.url}") String url,
                                                                   @Value("${mysql.datasource.driver}") String driver,
                                                                   @Value("${mysql.datasource.initialPoolSize}") int initialPoolSize,
                                                                   @Value("${mysql.datasource.minPoolSize}") int minPoolSize,
                                                                   @Value("${mysql.datasource.maxPoolSize}") int maxPoolSize,
                                                                   @Value("${mysql.datasource.maxIdleTime}") int maxIdleTime,
                                                                   @Value("${mysql.datasource.acquireRetryAttempts}") int acquireRetryAttempts,
                                                                   @Value("${mysql.datasource.breakAfterAcquireFailure}") boolean breakAfterAcquireFailure,
                                                                   @Value("${password.padding}") String padding,
                                                                   @Value("${password.key}") String key) throws PropertyVetoException {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();

        String mysqlPassword = passwordDecryptor.decrypt(padding, password, key);

        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(mysqlPassword);
        dataSource.setDriverClass(driver);
        dataSource.setInitialPoolSize(initialPoolSize);
        dataSource.setMinPoolSize(minPoolSize);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setMaxIdleTime(maxIdleTime);
        dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
        dataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);

        return dataSource;
    }
}