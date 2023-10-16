package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.Lock.LockManager;
import edu.unimelb.swen90007.mes.datamapper.SectionMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

@Getter @Setter
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

    public Section(Integer id, Event event, String name, Money money, Integer capacity) {
        this.id = id;
        this.event = event;
        this.name = name;
        this.money = money;
        this.capacity = capacity;
    }

    public Section(Event event, String name, Money money, Integer capacity, Integer remainingTickets) {
        this.event = event;
        this.name = name;
        this.money = money;
        this.capacity = capacity;
        this.remainingTickets = remainingTickets;
    }

    public Event loadEvent() {
        if (event == null)
            load();
        return event;
    }

    public String loadName() {
        if (name == null)
            load();
        return name;
    }

    public Money loadMoney() {
        if (money == null)
            load();
        return money;
    }

    public Integer loadCapacity() {
        if (capacity == null)
            load();
        return capacity;
    }

    public Integer loadRemainingTickets() {
        if (remainingTickets == null) {
            LockManager.getInstance().acquireTicketsReadLock(id);
            try{
                remainingTickets = SectionMapper.loadRemainingTickets(id);
            } catch (SQLException e) {
                logger.error(String.format("Error loading remaining tickets of the Section [id=%d]: %s",
                        id, e.getMessage()));
            } finally {
                LockManager.getInstance().releaseTicketsReadLock(id);
            }
        }
        return remainingTickets;
    }

    private void load() {
        logger.info("Loading Section [id=" + id + "]");
        try {
            Section section = SectionMapper.loadById(id);
            event = section.getEvent();
            name = section.getName();
            money = section.getMoney();
            capacity = section.getCapacity();
        } catch (SQLException e) {
            logger.error(String.format("Error loading Section [id=%d]: %s", id, e.getMessage()));
        }
    }
}
