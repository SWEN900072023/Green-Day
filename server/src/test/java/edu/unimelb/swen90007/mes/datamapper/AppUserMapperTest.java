package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.exceptions.AppUserAlreadyExistsException;
import edu.unimelb.swen90007.mes.exceptions.AppUserNotFoundException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

class AppUserMapperTest {
    @Test
    void testCreateEventPlanner() throws SQLException, AppUserAlreadyExistsException, AppUserNotFoundException {
        int id = 0;
        String email = "quanchi.chen.3@gmail.com";
        String password = "some.hash";
        String firstName = "Quanchi";
        String lastName = "Chen";

        EventPlanner eventPlanner = new EventPlanner(id, email, password, firstName, lastName);
        AppUserMapper.create(eventPlanner);

        Assertions.assertThrows(AppUserAlreadyExistsException.class, () -> AppUserMapper.create(eventPlanner));

        EventPlanner actualEventPlanner = (EventPlanner) AppUserMapper.loadByEmail(email);
        Assertions.assertEquals(eventPlanner.getId(), actualEventPlanner.getId());
        Assertions.assertEquals(email, actualEventPlanner.getEmail());
        Assertions.assertEquals(password, actualEventPlanner.getPassword());
        Assertions.assertEquals(firstName, actualEventPlanner.getFirstName());
        Assertions.assertEquals(lastName, actualEventPlanner.getLastName());

        AppUserMapper.delete(eventPlanner.getId());
    }

    @Test
    void testCreateCustomer() throws SQLException, AppUserAlreadyExistsException {
        int id = 0;
        String email = "quanchi.chen.3@gmail.com";
        String password = "some.hash";
        String firstName = "Quanchi";
        String lastName = "Chen";

        Customer customer = new Customer(id, email, password, firstName, lastName);
        AppUserMapper.create(customer);

        Assertions.assertThrows(AppUserAlreadyExistsException.class, () -> AppUserMapper.create(customer));

        List<AppUser> appUsers = AppUserMapper.loadAll();
        Assertions.assertEquals(1, appUsers.size());

        Customer actualCustomer = (Customer) appUsers.get(0);
        Assertions.assertEquals(customer.getId(), actualCustomer.getId());
        Assertions.assertEquals(email, actualCustomer.getEmail());
        Assertions.assertEquals(password, actualCustomer.getPassword());
        Assertions.assertEquals(firstName, actualCustomer.getFirstName());
        Assertions.assertEquals(lastName, actualCustomer.getLastName());

        AppUserMapper.delete(customer.getId());
    }
}