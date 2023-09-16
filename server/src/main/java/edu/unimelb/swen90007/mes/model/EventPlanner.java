package edu.unimelb.swen90007.mes.model;

public class EventPlanner extends AppUser {
    public EventPlanner(Integer id) {
        super(id);
    }

    public EventPlanner(Integer id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }

    public EventPlanner(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }
}
