package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import edu.unimelb.swen90007.mes.exceptions.AppUserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.service.CustomerServiceInterface;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;

public class CustomerService implements CustomerServiceInterface {
    @Override
    public void placeOrder(Order order) throws SQLException, AppUserAlreadyExistsException {
        UnitOfWork.getInstance().registerNew(order);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public List<Order> viewOwnOrder(Customer customer) throws SQLException {
        return OrderMapper.loadByCustomerID(customer.getId());
    }

    @Override
    public void cancelOrder(Order order) throws SQLException, AppUserAlreadyExistsException {
        UnitOfWork.getInstance().registerDirty(order);
        UnitOfWork.getInstance().commit();
    }
}
