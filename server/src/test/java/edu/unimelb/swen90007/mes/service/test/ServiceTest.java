package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import edu.unimelb.swen90007.mes.exceptions.CapacityExceedsException;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.exceptions.TimeConflictException;
import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.impl.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceTest {
    public static void main(String[] args)
            throws SQLException, CapacityExceedsException, PermissionDeniedException, TimeConflictException, UserAlreadyExistsException {

        // Create Service
        AppUserService appUserService = new AppUserService();
        PublicService publicService = new PublicService();
        AdminService adminService = new AdminService();
        EventPlannerService eventPlannerService = new EventPlannerService();
        CustomerService customerService = new CustomerService();

        // Create Test Cases
        Customer c1 = new Customer("XXX1@XXX.XXX", "123456", "XXX", "XXX");
        Customer c2 = new Customer("XXX2@XXX.XXX", "123456", "AAA", "AAA");

        EventPlanner ep1 = new EventPlanner("EEE1@XXX.XXX", "785699", "XXX", "XXX");
        EventPlanner ep2 = new EventPlanner("EEE2@XXX.XXX", "443215", "DDD", "DDD");

        //register
        appUserService.register(c1);
        appUserService.register(c2);
        appUserService.register(ep1);
        appUserService.register(ep2);


        // Create Test Cases
        Venue venue1 = new Venue("Name1", "Address1", 120);
        Venue venue2 = new Venue("Name2", "Address2", 120);

        OffsetDateTime now = OffsetDateTime.now();
        Event event1 = new Event("Title1", "Michael", venue1, now.plusDays(10), now.plusDays(11));
        Event event2 = new Event("Title2", "Michael", venue2, now.plusDays(10), now.plusDays(11));

        Money money = new Money(new BigDecimal(100), "Australia");
        List<Section> sections1 = new ArrayList<>();
        List<Section> sections2 = new ArrayList<>();
        sections1.add(new Section(event1, "Normal", money, 100, 0));
        sections1.add(new Section(event1, "VIP", money, 20, 0));
        sections2.add(new Section(event2, "Normal", money, 100, 0));
        sections2.add(new Section(event2, "VIP", money, 20, 0));
        event1.setSections(sections1);
        event2.setSections(sections2);
        event1.setFirstPlannerId(ep1.getId());
        event2.setFirstPlannerId(ep2.getId());

        // Create Test
        adminService.createVenue(venue1);
        adminService.createVenue(venue2);
        eventPlannerService.createEvent(event1);
        eventPlannerService.createEvent(event2);

        // Create Test Cases
        Order order11 = new Order(event1, c1);
        Order order12 = new Order(event1, c1);
        List<SubOrder> subOrders1 = new ArrayList<>();
        List<SubOrder> subOrders2 = new ArrayList<>();
        subOrders1.add(new SubOrder(sections1.get(0), 20, money));
        subOrders1.add(new SubOrder(sections1.get(1), 1, money));
        subOrders2.add(new SubOrder(sections1.get(0), 20, money));
        subOrders2.add(new SubOrder(sections1.get(1), 1, money));
        order11.setSubOrders(subOrders1);
        order12.setSubOrders(subOrders2);

        Order order21 = new Order(event2, c2);
        Order order22 = new Order(event2, c2);
        List<SubOrder> subOrders21 = new ArrayList<>();
        List<SubOrder> subOrders22 = new ArrayList<>();
        subOrders21.add(new SubOrder(sections2.get(0), 20, money));
        subOrders21.add(new SubOrder(sections2.get(1), 1, money));
        subOrders22.add(new SubOrder(sections2.get(0), 20, money));
        subOrders22.add(new SubOrder(sections2.get(1), 1, money));
        order21.setSubOrders(subOrders21);
        order22.setSubOrders(subOrders22);

        //Create Test
        customerService.placeOrder(order11);
        customerService.placeOrder(order12);
        customerService.placeOrder(order21);
        customerService.placeOrder(order22);

        // View Test
        publicService.viewAllEvents();
        publicService.viewNextSixMonthsEvents();
        publicService.searchEvents("Title");

        adminService.viewAllUsers();
        adminService.viewAllCustomers();
        adminService.viewAllEventPlanners();

        customerService.viewOwnOrder(c1);
        customerService.viewOwnOrder(c2);

        eventPlannerService.viewOrders(ep1, event1);
        eventPlannerService.viewOrders(ep2, event2);

        eventPlannerService.viewUninvitedEventPlanner(event1);

        eventPlannerService.viewHostedEvent(ep1);
        eventPlannerService.viewHostedEvent(ep2);

        // Update Test
        customerService.cancelOrder(order11);
        eventPlannerService.cancelOrder(ep1, order12);

        customerService.cancelOrder(order21);
        eventPlannerService.cancelOrder(ep2, order22);

        event1.setArtist("Tom");
        event1.setStartTime(OffsetDateTime.now().minusDays(2));
        event1.setEndTime(OffsetDateTime.now().minusDays(1));
        sections2.get(0).setCapacity(90);
        eventPlannerService.modifyEvent(ep1, event1);
        eventPlannerService.modifyEvent(ep2, event2);
        EventMapper.updateEndedEvent();

        ep1.setUserDetail("YYY@YYY.YYY", "abcdefg", "YYY", "YYY");
        publicService.modifyUser(ep1);

        eventPlannerService.inviteEventPlanner(ep1, ep2, event1);

        eventPlannerService.viewUninvitedEventPlanner(event1);

        // Delete Test
        adminService.deleteAppUser(c1);
        eventPlannerService.deleteEvent(ep1, event1);
        adminService.deleteVenue(venue2);
    }
}
