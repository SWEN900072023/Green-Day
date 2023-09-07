package edu.unimelb.swen90007.mes.model;

import java.util.List;

public class EventPlanner extends User {
    public EventPlanner(int id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }

    public void createEvent(Event event) {

    }

    public void modifyEvent(Event event) {

    }

    public void cancelEvent(int eventID) {

    }

    public void inviteEventPlanner(EventPlanner eventPlanner) {

    }

    public List<Event> viewAllHostedEvents() {
        return null;
    }

    public List<Order> viewOrders(int orderID) {
        return null;
    }

    public void deleteOrder(int orderID) {

    }
}
