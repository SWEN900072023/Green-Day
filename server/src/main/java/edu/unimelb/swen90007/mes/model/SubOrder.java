package edu.unimelb.swen90007.mes.model;

import lombok.Getter;

@Getter
public class SubOrder {
    private Order order;
    private final Section section;
    private final Integer quantity;
    private final Money money;

    public SubOrder(Order order, Section section, Integer quantity, Money money) {
        this.order = order;
        this.section = section;
        this.quantity = quantity;
        this.money = money;
    }

    public SubOrder(Section section, Integer quantity, Money money) {
        this.section = section;
        this.quantity = quantity;
        this.money = money;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
