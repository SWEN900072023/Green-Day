package edu.unimelb.swen90007.mes.model;

import java.time.OffsetDateTime;
import java.util.List;

public class Order {
    private final int id;
    private final Customer customer;
    private final Event event;
    private final List<Ticket> tickets;
    private final OffsetDateTime createdAt;
    private String status;

    public Order(int id, Customer customer, Event event, List<Ticket> tickets, OffsetDateTime createdAt, String status) {
        this.id = id;
        this.customer = customer;
        this.event = event;
        this.tickets = tickets;
        this.createdAt = createdAt;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Event getEvent() {
        return event;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
