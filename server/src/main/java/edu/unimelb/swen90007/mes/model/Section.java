package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.datamapper.SectionMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class Section {
    private static final Logger logger = LogManager.getLogger(Section.class);
    private Integer id;
    private Event event;
    private String name;
    private Money money;
    private Integer capacity;
    private Integer remainingTickets;

    public Section(int id) {
        this.id = id;
    }

    public Section(int id, Event event, String name, Money money, int capacity, int remainingTickets) {
        this.id = id;
        this.event = event;
        this.name = name;
        this.money = money;
        this.capacity = capacity;
        this.remainingTickets = remainingTickets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        if (event == null)
            load();
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        if (name == null)
            load();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getMoney() {
        if (money == null)
            load();
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public int getCapacity() {
        if (capacity == null)
            load();
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRemainingTickets() {
        if (remainingTickets == null)
            load();
        return remainingTickets;
    }

    public void setRemainingTickets(int remainingTickets) {
        this.remainingTickets = remainingTickets;
    }

    private void load() {
        logger.info("Loading Section [id=" + id + "]");
        try {
            Section section = SectionMapper.loadById(id);
            event = section.getEvent();
            name = section.getName();
            money = section.getMoney();
            capacity = section.getCapacity();
            remainingTickets = section.getRemainingTickets();
        } catch (SQLException e) {
            logger.error(String.format("Error loading Section [id=%d]: %s", id, e.getMessage()));
        }
    }
}
