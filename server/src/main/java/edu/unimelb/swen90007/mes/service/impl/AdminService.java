package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.AppUserMapper;
import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import edu.unimelb.swen90007.mes.datamapper.SectionMapper;
import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.AdminServiceInterface;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminService implements AdminServiceInterface {
    @Override
    public ArrayList<AppUser> viewAllEventPlanners() throws SQLException {
        return (ArrayList<AppUser>) AppUserMapper.loadAllEventPlanners();
    }

    @Override
    public ArrayList<AppUser> viewAllCustomers() throws SQLException {
        return (ArrayList<AppUser>) AppUserMapper.loadAllCustomer();
    }

    @Override
    public ArrayList<AppUser> viewAllUsers() throws SQLException {
        return (ArrayList<AppUser>) AppUserMapper.loadAll();
    }

    @Override
    public void createVenue(Venue venue) throws SQLException, UserAlreadyExistsException {
        UnitOfWork.getInstance().registerNew(venue);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public void deleteVenue(Venue venue) throws SQLException, UserAlreadyExistsException {
        List<Event> events = EventMapper.loadByVenue(venue);
        List<Section> sections = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        for(Event e : events) {
            sections.addAll(SectionMapper.loadSectionsByEventId(e.getId()));
            orders.addAll(OrderMapper.loadByEventId(e.getId()));
        }
        for(Order o : orders) {
            UnitOfWork.getInstance().registerDeleted(o);
        }
        for(Section s : sections) {
            UnitOfWork.getInstance().registerDeleted(s);
        }
        for(Event e : events) {
            UnitOfWork.getInstance().registerDeleted(e);
        }
        UnitOfWork.getInstance().registerDeleted(venue);

        UnitOfWork.getInstance().commit();
    }

    @Override
    public void deleteAppUser(AppUser user) throws SQLException, UserAlreadyExistsException {
        if (user instanceof Customer) {
            List<Order> orders = OrderMapper.loadByCustomerID(user.getId());
            for(Order o : orders)
                UnitOfWork.getInstance().registerDeleted(o);
        }
        UnitOfWork.getInstance().registerDeleted(user);
        UnitOfWork.getInstance().commit();
    }
}
