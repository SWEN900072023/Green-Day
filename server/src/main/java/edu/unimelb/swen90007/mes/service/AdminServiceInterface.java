package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.AppUserAlreadyExistsException;
import edu.unimelb.swen90007.mes.exceptions.AppUserNotFoundException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Venue;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AdminServiceInterface {
    public  ArrayList<AppUser> viewAllEventPlanners() throws SQLException, AppUserNotFoundException;
    public ArrayList<AppUser> viewAllCustomers() throws SQLException, AppUserNotFoundException;
    public ArrayList<AppUser> viewAllUsers() throws SQLException;
    public void createVenue(Venue venue) throws SQLException, AppUserAlreadyExistsException;
    public void deleteVenue(Venue venue) throws SQLException, AppUserAlreadyExistsException;
    public void deleteAppUser(AppUser user) throws SQLException, AppUserAlreadyExistsException;
}
