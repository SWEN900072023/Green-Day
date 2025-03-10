package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.Lock.LockManager;
import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.datamapper.*;
import edu.unimelb.swen90007.mes.exceptions.CapacityExceedsException;
import edu.unimelb.swen90007.mes.exceptions.InvalidTimeRangeException;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.exceptions.TimeConflictException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.IEventPlannerService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;

public class EventPlannerService implements IEventPlannerService {
    @Override
    public void createEvent(Event event)
            throws SQLException, CapacityExceedsException, TimeConflictException, InvalidTimeRangeException {
        if (invalidTimeRange(event))
            throw new InvalidTimeRangeException();
        if (isCapacityExceeded(event))
            throw new CapacityExceedsException();
        UnitOfWork.getInstance().registerNew(event);
        for (Section section : event.getSections()) {
            section.setEvent(event);
            UnitOfWork.getInstance().registerNew(section);
        }
        UnitOfWork.getInstance().commit();
    }

    @Override
    public void modifyEvent(EventPlanner ep, Event event)
            throws SQLException, CapacityExceedsException,
            PermissionDeniedException, TimeConflictException, InvalidTimeRangeException {
        if (invalidTimeRange(event))
            throw new InvalidTimeRangeException();
        if (!PlannerEventMapper.checkRelation(ep, event))
            throw new PermissionDeniedException();
        if (isCapacityExceeded(event))
            throw new CapacityExceedsException();

        UnitOfWork.getInstance().registerDirty(event);
        for (Section section : event.getSections()) {
            UnitOfWork.getInstance().registerDirty(section);
        }
        if (event.getStatus().equals(Constant.EVENT_CANCELLED)) {
            List<Order> orders = OrderMapper.loadByEventId(event.getId());
            for (Order order : orders) {
                PublicService.cancelOrder(order);
            }
        }

        UnitOfWork.getInstance().commit();
    }

    @Override
    public List<Event> viewHostedEvent(EventPlanner ep)
            throws SQLException {
        return EventMapper.loadByEventPlanner(ep);
    }

    @Override
    public List<AppUser> viewUninvitedEventPlanner(Event e) throws SQLException {
        return AppUserMapper.loadUninvitedEventPlanners(e);
    }

    @Override
    public void inviteEventPlanner(EventPlanner inviter, EventPlanner invitee, Event event)
            throws SQLException, PermissionDeniedException {
        if (!PlannerEventMapper.checkRelation(inviter, event))
            throw new PermissionDeniedException();
        PlannerEventMapper.inviteEventPlanner(invitee, event);
    }

    @Override
    public List<Order> viewOrders(EventPlanner ep, Event event)
            throws SQLException, PermissionDeniedException {
        if (!PlannerEventMapper.checkRelation(ep, event))
            throw new PermissionDeniedException();
        return OrderMapper.loadByEventId(event.getId());
    }

    @Override
    public void cancelOrder(EventPlanner ep, Order order)
            throws SQLException, PermissionDeniedException {
        if (!PlannerEventMapper.checkRelation(ep, order.loadEvent()))
            throw new PermissionDeniedException();
        LockManager.getInstance().acquireTicketsWriteLock(order);
        try {
            PublicService.cancelOrder(order);
            UnitOfWork.getInstance().commit();
        } finally {
            LockManager.getInstance().releaseTicketsWriteLock(order);
        }
    }

    private boolean isCapacityExceeded(Event event) {
        int eventCapacity = 0;
        for (Section s : event.loadSections())
            eventCapacity += s.loadCapacity();
        int venueCapacity = event.loadVenue().loadCapacity();
        return (eventCapacity > venueCapacity);
    }

    private boolean invalidTimeRange(Event event) {
        return event.getStartTime().isAfter(event.getEndTime());
    }
}
