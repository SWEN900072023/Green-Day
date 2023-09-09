package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.exceptions.UserNotFoundException;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class UserMapperTest {
    @Test
    void testCreateEventPlanner() throws SQLException, UserAlreadyExistsException, UserNotFoundException {
        int id = 0;
        String email = "quanchi.chen.3@gmail.com";
        String password = "some.hash";
        String firstName = "Quanchi";
        String lastName = "Chen";

        EventPlanner eventPlanner = new EventPlanner(id, email, password, firstName, lastName);
        UserMapper.create(eventPlanner);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> UserMapper.create(eventPlanner));

        EventPlanner actualEventPlanner = (EventPlanner) UserMapper.loadByEmail(email);
        Assertions.assertEquals(eventPlanner.getID(), actualEventPlanner.getID());
        Assertions.assertEquals(email, actualEventPlanner.getEmail());
        Assertions.assertEquals(password, actualEventPlanner.getPassword());
        Assertions.assertEquals(firstName, actualEventPlanner.getFirstName());
        Assertions.assertEquals(lastName, actualEventPlanner.getLastName());

        UserMapper.deleteByID(eventPlanner.getID());
    }

    @Test
    void testCreateCustomer() throws SQLException, UserAlreadyExistsException, UserNotFoundException {
        int id = 0;
        String email = "quanchi.chen.3@gmail.com";
        String password = "some.hash";
        String firstName = "Quanchi";
        String lastName = "Chen";

        Customer customer = new Customer(id, email, password, firstName, lastName);
        UserMapper.create(customer);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> UserMapper.create(customer));

        Customer actualCustomer = (Customer) UserMapper.loadByEmail(email);
        Assertions.assertEquals(customer.getID(), actualCustomer.getID());
        Assertions.assertEquals(email, actualCustomer.getEmail());
        Assertions.assertEquals(password, actualCustomer.getPassword());
        Assertions.assertEquals(firstName, actualCustomer.getFirstName());
        Assertions.assertEquals(lastName, actualCustomer.getLastName());

        UserMapper.deleteByID(customer.getID());
    }
}