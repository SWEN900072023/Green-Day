package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerService {
    void placeOrder(Order order) throws SQLException, UserAlreadyExistsException;
    List<Order> viewOwnOrder(Customer customer) throws SQLException;
    void cancelOrder(Order order) throws SQLException, UserAlreadyExistsException;
}
