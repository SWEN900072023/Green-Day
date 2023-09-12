package edu.unimelb.swen90007.mes.model;

import java.util.List;

public class Customer extends AppUser {
    public Customer(int id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }

    public List<Event> searchEvent(String eventName) {
        return null;
    }

    public List<Event> viewCalendarEvents() {
        return null;
    }

    public Event viewEvent(int eventID) {
        return null;
    }

    public void placeOrder(Order order) {

    }

    public List<Order> viewOwnOrders() {
        return null;
    }

    public void deleteOrder(int orderID) {

    }
}
