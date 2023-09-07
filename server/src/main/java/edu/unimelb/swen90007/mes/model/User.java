package edu.unimelb.swen90007.mes.model;

import java.util.List;

public abstract class User {
    private final int id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;

    public User(int id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<Event> viewAllEvents() {
        return null;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
