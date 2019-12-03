package com.speedment.example.joins;

import com.speedment.enterprise.datastore.runtime.DataStoreBundle;
import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import com.speedment.example.joins.db.ShippingApplication;
import com.speedment.example.joins.db.ShippingApplicationBuilder;
import com.speedment.example.joins.db.leg.LegManager;
import com.speedment.example.joins.db.order.OrderManager;
import com.speedment.example.joins.db.shipping.ShippingManager;
import com.speedment.runtime.join.JoinComponent;
import com.speedment.runtime.join.JoinBundle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Emil Forslund
 * @since  1.2.0
 */
@Configuration
public class SpeedmentConfig {

    @Bean(destroyMethod = "close")
    ShippingApplication application(Environment env) {
        return new ShippingApplicationBuilder()
            .withUsername(env.getRequiredProperty("speedment.username"))
            .withPassword(env.getRequiredProperty("speedment.password"))
            .withBundle(JoinBundle.class)
            .withBundle(DataStoreBundle.class)
            .build();
    }

    @Bean
    DataStoreComponent dataStoreComponent(ShippingApplication app) {
        return app.getOrThrow(DataStoreComponent.class);
    }

    @Bean
    JoinComponent joinComponent(ShippingApplication app) {
        return app.getOrThrow(JoinComponent.class);
    }

    @Bean
    LegManager legManager(ShippingApplication app) {
        return app.getOrThrow(LegManager.class);
    }

    @Bean
    OrderManager orderManager(ShippingApplication app) {
        return app.getOrThrow(OrderManager.class);
    }

    @Bean
    ShippingManager shippingManager(ShippingApplication app) {
        return app.getOrThrow(ShippingManager.class);
    }
}