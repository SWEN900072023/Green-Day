package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.AppUserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Event;

import java.sql.SQLException;
import java.util.List;

public interface PublicServiceInterface {
    public List<Event> viewAllEvents() throws SQLException;
    public List<Event> searchEvents(String pattern) throws SQLException;
    public void register(AppUser user) throws SQLException, AppUserAlreadyExistsException;
    public void modifyUser(AppUser user) throws SQLException, AppUserAlreadyExistsException;
}
