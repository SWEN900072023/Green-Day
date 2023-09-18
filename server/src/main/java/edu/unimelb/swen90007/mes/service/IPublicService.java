package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.Venue;

import java.sql.SQLException;
import java.util.List;

public interface IPublicService {
    List<Event> viewAllEvents() throws SQLException;
    List<Event> viewNextSixMonthsEvents() throws SQLException;
    List<Event> searchEvents(String pattern) throws SQLException;
    Event viewEventDetail(Event event) throws SQLException;
    List<Venue> viewAllVenues() throws SQLException;
    void modifyUser(AppUser user) throws SQLException, UserAlreadyExistsException;
}
