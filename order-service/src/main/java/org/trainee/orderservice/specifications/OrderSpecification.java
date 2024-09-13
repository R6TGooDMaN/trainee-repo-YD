package org.trainee.orderservice.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.trainee.orderservice.enums.OrderStatuses;
import org.trainee.orderservice.model.Order;

import java.time.LocalDate;

public class OrderSpecification {
    public static Specification<Order> hasStatus(OrderStatuses status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("orderStatus"), status);
    }

    public static Specification<Order> hasDate(LocalDate date) {
        return (root, query, cb) -> date == null ? null : cb.equal(root.get("orderDate"), date);
    }

    public static Specification<Order> orderByDateAsc() {
        return (root, query, cb) -> {
            query.orderBy(cb.asc(root.get("orderDate")));
            return null;
        };
    }

    public static Specification<Order> orderByDateDesc() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("orderDate")));
            return null;
        };
    }
}
