package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.AppUserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface CustomerServiceInterface {
    void placeOrder(Order order) throws SQLException, AppUserAlreadyExistsException;
    List<Order> viewOwnOrder(Customer customer) throws SQLException;
    void cancelOrder(Order order) throws SQLException, AppUserAlreadyExistsException;
}
