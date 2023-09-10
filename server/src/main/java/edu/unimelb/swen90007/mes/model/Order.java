package edu.unimelb.swen90007.mes.model;

import java.time.OffsetDateTime;
import java.util.List;

public class Order {
    private final Customer customer;
    private final List<SubOrder> subOrders;
    private final OffsetDateTime createdAt;
    private int id;
    private String status;

    public Order(int id, Customer customer, List<SubOrder> subOrders, OffsetDateTime createdAt, String status) {
        this.id = id;
        this.customer = customer;
        this.subOrders = subOrders;
        this.createdAt = createdAt;
        this.status = status;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<SubOrder> getSubOrders() {
        return subOrders;
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
