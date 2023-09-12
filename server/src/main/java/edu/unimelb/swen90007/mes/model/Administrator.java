package edu.unimelb.swen90007.mes.model;

import java.util.List;

public class Administrator extends AppUser {
    public Administrator(int id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }

    public void createVenue(Venue venue) {

    }

    public void createEventPlanner(EventPlanner eventPlanner) {

    }

    public List<AppUser> viewAllUsers() {
        return null;
    }
}
