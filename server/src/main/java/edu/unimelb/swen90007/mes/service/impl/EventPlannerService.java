package edu.unimelb.swen90007.mes.service.impl;

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
        if(invalidTimeRange(event))
            throw new InvalidTimeRangeException();
        if(isCapacityExceeded(event))
            throw new CapacityExceedsException();
        if(EventMapper.doesTimeConflict(event))
            throw new TimeConflictException();
        List<Section> sections = event.getSections();
        UnitOfWork.getInstance().registerNew(event);
        for (Section section : sections) {
            section.setEvent(event);
            UnitOfWork.getInstance().registerNew(section);
        }
        UnitOfWork.getInstance().commit();
    }

    @Override
    public void modifyEvent(EventPlanner ep, Event event)
            throws SQLException, CapacityExceedsException,
            PermissionDeniedException, TimeConflictException, InvalidTimeRangeException {
        if(invalidTimeRange(event))
            throw new InvalidTimeRangeException();
        if(!PlannerEventMapper.checkRelation(ep, event))
            throw new PermissionDeniedException();
        if(isCapacityExceeded(event))
            throw new CapacityExceedsException();
        if(EventMapper.doesTimeConflict(event))
            throw new TimeConflictException();

        UnitOfWork.getInstance().registerDirty(event);
        for (Section section : event.getSections()) {
            UnitOfWork.getInstance().registerDirty(section);
        }
        if (event.getStatus().equals(Constant.EVENT_CANCELLED)) {
            List<Order> orders = OrderMapper.loadByEventId(event.getId());
            for(Order order : orders) {
                UnitOfWork.getInstance().registerDirty(order);
            }
        }

        UnitOfWork.getInstance().commit();
    }

    @Override
    public void deleteEvent(EventPlanner ep, Event event)
            throws SQLException, PermissionDeniedException {
        if(!PlannerEventMapper.checkRelation(ep, event))
            throw new PermissionDeniedException();
        List<Section> sections = SectionMapper.loadSectionsByEventId(event.getId());
        List<Order> orders = OrderMapper.loadByEventId(event.getId());
        for(Order o : orders) {
            UnitOfWork.getInstance().registerDeleted(o);
        }
        for(Section s : sections) {
            UnitOfWork.getInstance().registerDeleted(s);
        }
        UnitOfWork.getInstance().registerDeleted(event);

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
        if(!PlannerEventMapper.checkRelation(inviter, event))
            throw new PermissionDeniedException();
        PlannerEventMapper.inviteEventPlanner(invitee, event);
    }

    @Override
    public List<Order> viewOrders(EventPlanner ep, Event event)
            throws SQLException, PermissionDeniedException {
        if(!PlannerEventMapper.checkRelation(ep, event))
            throw new PermissionDeniedException();
        return OrderMapper.loadByEventId(event.getId());
    }

    @Override
    public void cancelOrder(EventPlanner ep, Order order)
            throws SQLException, PermissionDeniedException {
        if(!PlannerEventMapper.checkRelation(ep, order.loadEvent()))
            throw new PermissionDeniedException();
        UnitOfWork.getInstance().registerDirty(order);
        for (SubOrder subOrder : order.loadSubOrders()) {
            Section section = subOrder.getSection();
            int remainingTickets = section.loadRemainingTickets();
            section.setRemainingTickets(remainingTickets + subOrder.getQuantity());
            UnitOfWork.getInstance().registerDirty(section);
        }
        UnitOfWork.getInstance().commit();
    }

    private boolean isCapacityExceeded(Event event) {
        int eventCapacity = 0;
        for(Section s : event.loadSections())
            eventCapacity += s.loadCapacity();
        int venueCapacity = event.loadVenue().loadCapacity();
        return (eventCapacity > venueCapacity);
    }

    public boolean invalidTimeRange(Event event){
        return event.getStartTime().isAfter(event.getEndTime());
    }
}
