package edu.unimelb.swen90007.mes.model;

public class Customer extends AppUser {
    public Customer(Integer id) {
        super(id);
    }

    public Customer(Integer id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }

    public Customer(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }
}
