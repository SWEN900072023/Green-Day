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

    public Section(Integer id) {
        this.id = id;
    }

    public Section(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Section(Integer id, Event event, String name, Money money, Integer capacity, Integer remainingTickets) {
        this.id = id;
        this.event = event;
        this.name = name;
        this.money = money;
        this.capacity = capacity;
        this.remainingTickets = remainingTickets;
    }

    public Section(Event event, String name, Money money, Integer capacity, Integer remainingTickets) {
        this.event = event;
        this.name = name;
        this.money = money;
        this.capacity = capacity;
        this.remainingTickets = remainingTickets;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getCapacity() {
        if (capacity == null)
            load();
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getRemainingTickets() {
        if (remainingTickets == null)
            load();
        return remainingTickets;
    }

    public void setRemainingTickets(Integer remainingTickets) {
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
