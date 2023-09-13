package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.AppUserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface CustomerServiceInterface {
    public void placeOrder(Order order) throws SQLException, AppUserAlreadyExistsException;
    public List<Order> viewOwnOrder(Customer customer) throws SQLException;
    public void cancelOrder(Order order) throws SQLException, AppUserAlreadyExistsException;
    public void deleteOrder(Order order) throws SQLException, AppUserAlreadyExistsException;
}
