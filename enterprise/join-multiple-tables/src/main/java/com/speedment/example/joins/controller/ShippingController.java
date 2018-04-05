package com.speedment.example.joins.controller;

import com.speedment.example.joins.db.leg.Leg;
import com.speedment.example.joins.db.order.Order;
import com.speedment.example.joins.db.shipping.Shipping;
import com.speedment.example.joins.db.shipping.ShippingManager;
import com.speedment.runtime.join.Join;
import com.speedment.runtime.join.JoinComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparingInt;

/**
 * @author Emil Forslund
 * @since  1.2.0
 */
@RestController
@RequestMapping("shipping")
public class ShippingController {

    private final static Comparator<ShippingView> MINIMIZE_COST =
        comparingInt(view -> view.orderValue - view.totalCost);

    private @Autowired ShippingManager shippings;
    private @Autowired JoinComponent joins;

    private Join<ShippingView> join;

    @PostConstruct
    void createJoin() {
        join = joins.from(ShippingManager.IDENTIFIER)
            .leftJoinOn(Order.ID).equal(Shipping.ORDER)
            .leftJoinOn(Leg.ID).equal(Shipping.LEG1)
            .leftJoinOn(Leg.ID).equal(Shipping.LEG2)
            .leftJoinOn(Leg.ID).equal(Shipping.LEG3)
            .leftJoinOn(Leg.ID).equal(Shipping.LEG4)
            .build(ShippingView::new);
    }

    @GetMapping
    List<ShippingView> findNext() {
        return join.stream()
            .min(MINIMIZE_COST)
            .map(Collections::singletonList)
            .orElseGet(Collections::emptyList);
    }

    private final static class ShippingView {
        private final long id;
        private final int orderValue;
        private final String orderName;
        private final String orderComment;
        private final List<String> legs;
        private final int totalCost;

        ShippingView(Shipping shipping, Order order,
                     Leg leg1, Leg leg2,
                     Leg leg3, Leg leg4) {
            this.id           = shipping.getId();
            this.orderName    = order.getName();
            this.orderValue   = order.getValue();
            this.orderComment = order.getComment().orElse(null);
            this.legs         = new ArrayList<>();

            int cost = 0;
            if (leg1 != null) {
                legs.add(leg1.getFrom());
                legs.add(leg1.getTo());
                cost += leg1.getCost();

                if (leg2 != null) {
                    legs.add(leg2.getTo());
                    cost += leg2.getCost();

                    if (leg3 != null) {
                        legs.add(leg3.getTo());
                        cost += leg3.getCost();

                        if (leg4 != null) {
                            legs.add(leg4.getTo());
                            cost += leg4.getCost();
                        }
                    }
                }
            }

            this.totalCost = cost;
        }

        public long getId() {
            return id;
        }

        public int getOrderValue() {
            return orderValue;
        }

        public String getOrderName() {
            return orderName;
        }

        public String getOrderComment() {
            return orderComment;
        }

        public List<String> getLegs() {
            return legs;
        }

        public int getTotalCost() {
            return totalCost;
        }
    }
}
