package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.AppUserMapper;
import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import edu.unimelb.swen90007.mes.datamapper.VenueMapper;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.Venue;
import edu.unimelb.swen90007.mes.service.IPublicService;

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
        return EventMapper.loadByIdAll(event.getId());
    }

    @Override
    public List<Venue> viewAllVenues() throws SQLException {
        return VenueMapper.loadAll();
    }

    @Override
    public void modifyUser(AppUser user) throws SQLException {
        AppUserMapper.update(user);
    }

}
