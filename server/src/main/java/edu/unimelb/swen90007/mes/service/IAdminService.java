package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Venue;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IAdminService {
    ArrayList<AppUser> viewAllEventPlanners() throws SQLException;
    ArrayList<AppUser> viewAllCustomers() throws SQLException;
    ArrayList<AppUser> viewAllUsers() throws SQLException;
    void createVenue(Venue venue) throws SQLException, UserAlreadyExistsException;
    void deleteVenue(Venue venue) throws SQLException, UserAlreadyExistsException;
    void deleteAppUser(AppUser user) throws SQLException, UserAlreadyExistsException;
}
