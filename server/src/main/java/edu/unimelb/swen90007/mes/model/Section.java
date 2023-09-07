package edu.unimelb.swen90007.mes.model;

public class Section {
    private final int id;
    private final Event event;
    private String name;
    private Money money;
    private int capacity;
    private int remainingTickets;

    public Section(int id, Event event, String name, Money money, int capacity, int remainingTickets) {
        this.id = id;
        this.event = event;
        this.name = name;
        this.money = money;
        this.capacity = capacity;
        this.remainingTickets = remainingTickets;
    }

    public int getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRemainingTickets() {
        return remainingTickets;
    }

    public void setRemainingTickets(int remainingTickets) {
        this.remainingTickets = remainingTickets;
    }
}
