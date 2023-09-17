package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.service.ICustomerService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CustomerService implements ICustomerService {
    @Override
    public void placeOrder(Order order) throws SQLException, UserAlreadyExistsException {
        UnitOfWork.getInstance().registerNew(order);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public List<Order> viewOwnOrder(Customer customer) throws SQLException {
        return OrderMapper.loadByCustomerID(customer.getId());
    }

    @Override
    public void cancelOrder(Customer customer, Order order)
            throws SQLException, UserAlreadyExistsException, PermissionDeniedException {
        if (!Objects.equals(customer.getId(), order.getCustomer().getId()))
            throw new PermissionDeniedException();
        UnitOfWork.getInstance().registerDirty(order);
        UnitOfWork.getInstance().commit();
    }
}
