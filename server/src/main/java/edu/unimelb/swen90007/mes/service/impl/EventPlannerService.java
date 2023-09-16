package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import edu.unimelb.swen90007.mes.datamapper.PlannerEventMapper;
import edu.unimelb.swen90007.mes.datamapper.SectionMapper;
import edu.unimelb.swen90007.mes.exceptions.CapacityExceedsException;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.exceptions.TimeConflictException;
import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.model.Section;
import edu.unimelb.swen90007.mes.service.IEventPlannerService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;

public class EventPlannerService implements IEventPlannerService {
    @Override
    public void createEvent(Event event)
            throws SQLException, CapacityExceedsException, TimeConflictException, UserAlreadyExistsException {
        if(capacityCheck(event))
            throw new CapacityExceedsException();
        if(EventMapper.timeCheck(event))
            throw new TimeConflictException();
        UnitOfWork.getInstance().registerNew(event);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public void modifyEvent(EventPlanner ep, Event event)
            throws SQLException, CapacityExceedsException,
            PermissionDeniedException, TimeConflictException, UserAlreadyExistsException {
        if(!PlannerEventMapper.checkRelation(ep, event))
            throw new PermissionDeniedException();
        if(capacityCheck(event))
            throw new CapacityExceedsException();
        if(EventMapper.timeCheck(event))
            throw new TimeConflictException();
        UnitOfWork.getInstance().registerDirty(event);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public void deleteEvent(EventPlanner ep, Event event)
            throws SQLException, PermissionDeniedException, UserAlreadyExistsException {
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
            throws SQLException, PermissionDeniedException, UserAlreadyExistsException {
        if(!PlannerEventMapper.checkRelation(ep, order.getEvent()))
            throw new PermissionDeniedException();
        UnitOfWork.getInstance().registerDirty(order);
        UnitOfWork.getInstance().commit();
    }

    public boolean capacityCheck(Event event) throws SQLException {
        int eventCapacity = 0;
        for(Section s : event.getSections())
            eventCapacity += s.getCapacity();
        int venueCapacity = event.getVenue().getCapacity();
        return (eventCapacity > venueCapacity);
    }
}
