package edu.unimelb.swen90007.mes.model;

public class SubOrder {
    private final Section section;
    private final int quantity;
    private final Money money;

    public SubOrder(Section section, int quantity, Money money) {
        this.section = section;
        this.quantity = quantity;
        this.money = money;
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
