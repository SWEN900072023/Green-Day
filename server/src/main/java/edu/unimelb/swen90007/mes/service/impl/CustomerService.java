package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.Lock.LockManager;
import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.exceptions.TicketInsufficientException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.ICustomerService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CustomerService implements ICustomerService {
    @Override
    public void placeOrder(Order order) throws TicketInsufficientException {
        LockManager.getInstance().acquireTicketsWriteLock(order);
        try {
            UnitOfWork.getInstance().registerNew(order);
            for (SubOrder subOrder : order.getSubOrders()) {
                subOrder.setOrder(order);
                UnitOfWork.getInstance().registerNew(subOrder);
                Section section = subOrder.getSection();
                int remainingTickets = section.loadRemainingTickets();
                if (remainingTickets < subOrder.getQuantity()) {
                    UnitOfWork.getInstance().clear();
                    throw new TicketInsufficientException();
                }
                SectionTickets sectionTickets =
                        new SectionTickets(section.getId(), remainingTickets - subOrder.getQuantity());
                UnitOfWork.getInstance().registerDirty(sectionTickets);
            }
            UnitOfWork.getInstance().commit();
        } finally {
            LockManager.getInstance().releaseTicketsWriteLock(order);
        }
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
        LockManager.getInstance().acquireTicketsWriteLock(order);
        try {
            PublicService.cancelOrder(order);
            UnitOfWork.getInstance().commit();
        } finally {
            LockManager.getInstance().releaseTicketsWriteLock(order);
        }
    }
}
