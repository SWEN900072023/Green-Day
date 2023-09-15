package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Event;

import java.sql.SQLException;
import java.util.List;

public interface PublicServiceInterface {
    List<Event> viewAllEvents() throws SQLException;
    List<Event> viewNextSixMothsEvents() throws SQLException;
    List<Event> searchEvents(String pattern) throws SQLException;
    void register(AppUser user) throws SQLException, UserAlreadyExistsException;
    void modifyUser(AppUser user) throws SQLException, UserAlreadyExistsException;
    String userAuthentication(AppUser user) throws SQLException;
}
