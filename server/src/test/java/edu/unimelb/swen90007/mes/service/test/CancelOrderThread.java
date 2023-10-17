package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.service.impl.CustomerService;

import java.sql.SQLException;
import java.util.List;

public class CancelOrderThread extends Thread {
    private final Customer customer;
    private final CustomerService customerService = new CustomerService();

    public CancelOrderThread(Customer customer) {
        this.customer = customer;
    }

    public void cancelOrder() {
        try {
            List<Order> orders = customerService.viewOwnOrder(customer);
            for (Order order : orders) {
                customerService.cancelOrder(customer, order);
            }
        } catch (SQLException | PermissionDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        cancelOrder();
    }
}
