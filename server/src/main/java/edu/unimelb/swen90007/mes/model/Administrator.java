package edu.unimelb.swen90007.mes.model;

public class Administrator extends AppUser {
    public Administrator(Integer id) {
        super(id);
    }

    public Administrator(Integer id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }
}
