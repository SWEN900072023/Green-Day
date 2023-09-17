package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.service.ICustomerService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;

public class CustomerService implements ICustomerService {
    @Override
    public void placeOrder(Order order) {
        UnitOfWork.getInstance().registerNew(order);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public List<Order> viewOwnOrder(Customer customer) throws SQLException {
        return OrderMapper.loadByCustomerID(customer.getId());
    }

    @Override
    public void cancelOrder(Order order) {
        UnitOfWork.getInstance().registerDirty(order);
        UnitOfWork.getInstance().commit();
    }
}
