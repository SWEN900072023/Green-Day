package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

class OrderMapperTest {
    static int capacitySection1;
    static int capacitySection2;
    static Venue venue;
    static EventPlanner eventPlanner;
    static Money money;
    static Event event;
    static Customer customer;

    @BeforeAll
    static void setup() throws SQLException, UserAlreadyExistsException {
        capacitySection1 = 1000;
        capacitySection2 = 2000;
        int totalCapacity = capacitySection1 + capacitySection2;

        venue = new Venue(0, "Mock Venue", "Mock Address", totalCapacity);
        VenueMapper.create(venue);

        eventPlanner = new EventPlanner(0, "event.planner@gmail.com", "mock.password", "Quanchi", "Chen");
        UserMapper.create(eventPlanner);

        List<Section> sections = new ArrayList<>();
        money = new Money(BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_UP), "AUD");
        sections.add(new Section(0, null, "Mock Section 1", money, capacitySection1, capacitySection1));
        sections.add(new Section(0, null, "Mock Section 2", money, capacitySection2, capacitySection2));

        event = new Event(0, sections, "Mock Event", "Mock Artist", venue, OffsetDateTime.now(), OffsetDateTime.now(), "Active");
        for (Section section : sections)
            section.setEvent(event);
        EventMapper.create(eventPlanner.getID(), event);

        customer = new Customer(0, "customer@gmail.com", "mock.password", "Quanchi", "Chen");
        UserMapper.create(customer);
    }

    @AfterAll
    static void cleanup() throws SQLException {
        for (Section section : event.getSections())
            SectionMapper.delete(section.getID());

        EventMapper.delete(event.getID(), eventPlanner.getID());
        UserMapper.delete(eventPlanner.getID());
        UserMapper.delete(customer.getID());
        VenueMapper.delete(venue.getID());
    }

    @Test
    void testCreate() throws SQLException {
        int id = 0;
        List<Section> sections = event.getSections();
        Section section1 = sections.get(0);
        int quantitySection1 = 100;
        Section section2 = sections.get(1);
        int quantitySection2 = 200;
        SubOrder subOrder1 = new SubOrder(section1, quantitySection1, money);
        SubOrder subOrder2 = new SubOrder(section2, quantitySection2, money);
        List<SubOrder> subOrders = new ArrayList<>();
        subOrders.add(subOrder1);
        subOrders.add(subOrder2);
        String status = "Active";

        Order order = new Order(id, customer, subOrders, OffsetDateTime.now(), status);
        OrderMapper.create(customer.getID(), order);

        List<Order> actualOrders = OrderMapper.loadByCustomerID(customer.getID());
        Order actualOrder = actualOrders.get(0);
        Assertions.assertEquals(order.getID(), actualOrder.getID());
        Assertions.assertEquals(customer.getID(), actualOrder.getCustomer().getID());
        Assertions.assertEquals(status, order.getStatus());

        List<SubOrder> actualSubOrders = actualOrder.getSubOrders();

        SubOrder actualSubOrder1 = actualSubOrders.get(0);
        Assertions.assertEquals(section1.getID(), actualSubOrder1.getSection().getID());
        Assertions.assertEquals(capacitySection1, actualSubOrder1.getSection().getCapacity());
        Assertions.assertEquals(quantitySection1, actualSubOrder1.getQuantity());
        Assertions.assertEquals(money.getUnitPrice(), actualSubOrder1.getMoney().getUnitPrice());
        Assertions.assertEquals(money.getCurrency(), actualSubOrder1.getMoney().getCurrency());

        SubOrder actualSubOrder2 = actualSubOrders.get(1);
        Assertions.assertEquals(section2.getID(), actualSubOrder2.getSection().getID());
        Assertions.assertEquals(capacitySection2, actualSubOrder2.getSection().getCapacity());
        Assertions.assertEquals(quantitySection2, actualSubOrder2.getQuantity());
        Assertions.assertEquals(money.getUnitPrice(), actualSubOrder2.getMoney().getUnitPrice());
        Assertions.assertEquals(money.getCurrency(), actualSubOrder2.getMoney().getCurrency());

        int expectedSection1RemainingTickets = capacitySection1 - quantitySection1; // 1000 - 100 = 900
        Assertions.assertEquals(expectedSection1RemainingTickets, actualSubOrder1.getSection().getRemainingTickets());

        int expectedSection2RemainingTickets = capacitySection2 - quantitySection2; // 2000 - 200 = 1800
        Assertions.assertEquals(expectedSection2RemainingTickets, actualSubOrder2.getSection().getRemainingTickets());

        OrderMapper.delete(order.getID());
    }
}