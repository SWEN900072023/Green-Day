package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.AppUserAlreadyExistsException;
import edu.unimelb.swen90007.mes.exceptions.AppUserNotFoundException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Venue;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AdminServiceInterface {
    ArrayList<AppUser> viewAllEventPlanners() throws SQLException, AppUserNotFoundException;
    ArrayList<AppUser> viewAllCustomers() throws SQLException, AppUserNotFoundException;
    ArrayList<AppUser> viewAllUsers() throws SQLException;
    void createVenue(Venue venue) throws SQLException, AppUserAlreadyExistsException;
    void deleteVenue(Venue venue) throws SQLException, AppUserAlreadyExistsException;
    void deleteAppUser(AppUser user) throws SQLException, AppUserAlreadyExistsException;
}
