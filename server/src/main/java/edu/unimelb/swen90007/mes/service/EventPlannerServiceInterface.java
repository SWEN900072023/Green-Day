package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.AppUserAlreadyExistsException;
import edu.unimelb.swen90007.mes.exceptions.CapacityExceedsException;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface EventPlannerServiceInterface {
    public void createEvent(Event event) throws SQLException, AppUserAlreadyExistsException, CapacityExceedsException;
    public void modifyEvent(EventPlanner ep, Event event) throws SQLException, AppUserAlreadyExistsException, CapacityExceedsException, PermissionDeniedException;
    public void deleteEvent(EventPlanner ep, Event event) throws SQLException, AppUserAlreadyExistsException, PermissionDeniedException;
    public List<Event> viewHostedEvent(EventPlanner ep) throws SQLException;
    public void inviteEventPlanner(EventPlanner inviter, EventPlanner invitee, Event event) throws SQLException, PermissionDeniedException;
    public List<Order> viewOrders(EventPlanner ep, Event event) throws SQLException, PermissionDeniedException;
    public void cancelOrder(EventPlanner ep, Order order) throws SQLException, PermissionDeniedException, AppUserAlreadyExistsException;
    public void deleteOrder(EventPlanner ep, Order order) throws SQLException, PermissionDeniedException, AppUserAlreadyExistsException;
}
