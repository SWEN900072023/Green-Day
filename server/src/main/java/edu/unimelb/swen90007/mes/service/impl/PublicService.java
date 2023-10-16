package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.Lock.LockManager;
import edu.unimelb.swen90007.mes.datamapper.AppUserMapper;
import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import edu.unimelb.swen90007.mes.datamapper.PlannerEventMapper;
import edu.unimelb.swen90007.mes.datamapper.VenueMapper;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.IPublicService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;

public class PublicService implements IPublicService {
    @Override
    public List<Event> viewAllEvents() throws SQLException {
        return EventMapper.loadAll();
    }

    @Override
    public List<Event> viewNextSixMonthsEvents() throws SQLException {
        return EventMapper.loadNextSixMonths();
    }

    @Override
    public List<Event> searchEvents(String pattern) throws SQLException {
        return EventMapper.loadByPattern(pattern);
    }

    @Override
    public Event viewEventDetail(Event event) throws SQLException {
        LockManager.getInstance().acquireTicketsReadLock(event);
        event = EventMapper.loadByIdAll(event.getId());
        LockManager.getInstance().releaseTicketsReadLock(event);
        return event;
    }

    @Override
    public List<Venue> viewAllVenues() throws SQLException {
        return VenueMapper.loadAll();
    }

    @Override
    public void modifyUser(AppUser user) throws SQLException {
        AppUserMapper.update(user);
    }

    public static void cancelOrder(Order order){
        UnitOfWork.getInstance().registerDirty(order);
        for (SubOrder subOrder : order.loadSubOrders()) {
            Section section = subOrder.getSection();
            int remainingTickets = section.loadRemainingTickets();
            SectionTickets sectionTickets =
                    new SectionTickets(section.getId(), remainingTickets + subOrder.getQuantity());
            UnitOfWork.getInstance().registerDirty(sectionTickets);
        }
    }

}
