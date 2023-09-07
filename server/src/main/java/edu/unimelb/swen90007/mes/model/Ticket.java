package edu.unimelb.swen90007.mes.model;

public class Ticket {
    private final Section section;
    private final Money money;

    public Ticket(Section section, Money money) {
        this.section = section;
        this.money = money;
    }

    public Section getSection() {
        return section;
    }

    public Money getMoney() {
        return money;
    }
}
