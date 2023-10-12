package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.exceptions.TicketInsufficientException;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.model.Section;
import edu.unimelb.swen90007.mes.model.SubOrder;
import edu.unimelb.swen90007.mes.service.ICustomerService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CustomerService implements ICustomerService {
    @Override
    public void placeOrder(Order order) throws TicketInsufficientException {
        if (!isTicketSufficient(order))
            throw new TicketInsufficientException();
        UnitOfWork.getInstance().registerNew(order);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public List<Order> viewOwnOrder(Customer customer) throws SQLException {
        return OrderMapper.loadByCustomerID(customer.getId());
    }

    @Override
    public void cancelOrder(Customer customer, Order order)
            throws PermissionDeniedException {
        if (!Objects.equals(customer.getId(), order.loadCustomer().getId()))
            throw new PermissionDeniedException();
        UnitOfWork.getInstance().registerDirty(order);
        UnitOfWork.getInstance().commit();
    }

    public boolean isTicketSufficient(Order order) {
        for(SubOrder so: order.getSubOrders()){
            Section se = so.getSection();
            int remainingTickets = se.loadRemainingTickets();
            int orderTickets = so.getQuantity();
            if (orderTickets > remainingTickets) return false;
        }
        return true;
    }
}
