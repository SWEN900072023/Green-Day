package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.exceptions.TicketInsufficientException;
import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.impl.AppUserService;
import edu.unimelb.swen90007.mes.service.impl.CustomerService;
import edu.unimelb.swen90007.mes.service.impl.PublicService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Thread representing a customer placing orders in the concurrency test.
 */
public class CustomerThread extends Thread {
    private static final int NUM_ORDERS = 50;
    private final Customer customer;
    private final PublicService publicService = new PublicService();
    private final CustomerService customerService = new CustomerService();
    private final Money money = new Money(new BigDecimal(100), "AUD");

    public CustomerThread(String email, String password, String firstName, String lastName) {
        customer = new Customer(email, password, firstName, lastName);
        try {
            AppUserService appUserService = new AppUserService();
            appUserService.register(customer);
        } catch (SQLException | UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer getMockCustomer() {
        return customer;
    }

    /**
     * Simulate a customer to place an order for a random event.
     */
    public void placeOrder() {
        try {
            List<Event> events = publicService.viewAllEvents();
            int size = events.size();
            int index = ThreadLocalRandom.current().nextInt(size);
            Event event = events.get(index); // A random event
            event = publicService.viewEventDetail(event);
            List<SubOrder> subOrders = new LinkedList<>();
            for (Section section : event.loadSections()) {
                SubOrder subOrder = new SubOrder(section, 4, money);
                subOrders.add(subOrder);
            }
            Order order = new Order(event, customer, subOrders);
            customerService.placeOrder(order);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (TicketInsufficientException e) {
            System.out.println(customer.getFirstName() + " Failed to buy tickets. The tickets is insufficient");
        }
    }

    /**
     * Simulate a customer to place NUM_ORDERS orders for potentially different events.
     */
    @Override
    public void run() {
        for (int i = 0; i < NUM_ORDERS; i++) {
            placeOrder();
        }
    }
}
