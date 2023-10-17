package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.CapacityExceedsException;
import edu.unimelb.swen90007.mes.exceptions.InvalidTimeRangeException;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.exceptions.TimeConflictException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface IEventPlannerService {
    void createEvent(Event event) throws SQLException, CapacityExceedsException, TimeConflictException, InvalidTimeRangeException;

    void modifyEvent(EventPlanner ep, Event event) throws SQLException, CapacityExceedsException, PermissionDeniedException, TimeConflictException, InvalidTimeRangeException;

    List<Event> viewHostedEvent(EventPlanner ep) throws SQLException;

    List<AppUser> viewUninvitedEventPlanner(Event e) throws SQLException;

    void inviteEventPlanner(EventPlanner inviter, EventPlanner invitee, Event event) throws SQLException, PermissionDeniedException;

    List<Order> viewOrders(EventPlanner ep, Event event) throws SQLException, PermissionDeniedException;

    void cancelOrder(EventPlanner ep, Order order) throws SQLException, PermissionDeniedException;
}
