package edu.unimelb.swen90007.mes.model;

public class SubOrder {
    private Integer orderId;
    private final Section section;
    private final Integer quantity;
    private final Money money;

    public SubOrder(Integer orderId, Section section, Integer quantity, Money money) {
        this.orderId = orderId;
        this.section = section;
        this.quantity = quantity;
        this.money = money;
    }

    public SubOrder(Section section, Integer quantity, Money money) {
        this.section = section;
        this.quantity = quantity;
        this.money = money;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Section getSection() {
        return section;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Money getMoney() {
        return money;
    }
}
