package edu.unimelb.swen90007.mes.model;

public class SubOrder {
    private int orderId;
    private final Section section;
    private final int quantity;
    private final Money money;

    public SubOrder(int orderId, Section section, int quantity, Money money) {
        this.orderId = orderId;
        this.section = section;
        this.quantity = quantity;
        this.money = money;
    }

    public SubOrder(Section section, int quantity, Money money) {
        this.section = section;
        this.quantity = quantity;
        this.money = money;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Section getSection() {
        return section;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getMoney() {
        return money;
    }
}
