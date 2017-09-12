package com.speedment.example.spring.aggregate.config;

import com.speedment.enterprise.datastore.runtime.DataStoreBundle;
import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import com.speedment.enterprise.plugins.json.JsonBundle;
import com.speedment.enterprise.plugins.json.JsonComponent;
import com.speedment.example.spring.aggregate.db.EmployeesApplication;
import com.speedment.example.spring.aggregate.db.EmployeesApplicationBuilder;
import com.speedment.example.spring.aggregate.db.SalaryManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static java.util.Objects.requireNonNull;

/**
 * @author Emil Forslund
 * @since  1.0.0
 */
@Configuration
public class SpeedmentConfig {

    private final Environment env;

    SpeedmentConfig(Environment env) {
        this.env = requireNonNull(env);
    }

    @Bean(destroyMethod = "stop")
    EmployeesApplication getApplication() {
        return new EmployeesApplicationBuilder()
            .withConnectionUrl(env.getProperty("spring.datasource.url"))
            .withUsername(env.getProperty("spring.datasource.username"))
            .withPassword(env.getProperty("spring.datasource.password"))
            .withBundle(DataStoreBundle.class)
            .withBundle(JsonBundle.class)
            .build();
    }

    @Bean
    DataStoreComponent getDataStoreComponent(EmployeesApplication app) {
        return app.getOrThrow(DataStoreComponent.class);
    }

    @Bean
    JsonComponent getJsonComponent(EmployeesApplication app) {
        return app.getOrThrow(JsonComponent.class);
    }

    @Bean
    SalaryManager getSalaryManager(EmployeesApplication app) {
        return app.getOrThrow(SalaryManager.class);
    }
}

