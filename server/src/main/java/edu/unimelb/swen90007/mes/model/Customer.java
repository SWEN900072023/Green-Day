package edu.unimelb.swen90007.mes.model;

public class Customer extends AppUser {
    public Customer(int id) {
        super(id);
    }

    public Customer(int id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }

    public Customer(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }
}
