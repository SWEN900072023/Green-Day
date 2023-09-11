package edu.unimelb.swen90007.mes.model;

public class Customer extends User {
    public Customer(int id) {
        super(id);
    }

    public Customer(int id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }
}
