package ru.strela.model.filter;

public class Order {

    private String field;
    private OrderDirection direction;

    public Order(String field, OrderDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public OrderDirection getDirection() {
        return direction;
    }
}
