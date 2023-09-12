package edu.unimelb.swen90007.mes.model;

public class EventPlanner extends AppUser {
    public EventPlanner(int id) {
        super(id);
    }

    public EventPlanner(int id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }
}
