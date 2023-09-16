package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

@Getter @Setter
public class Order {
    private static final Logger logger = LogManager.getLogger(Order.class);
    private Integer id;
    private Event event;
    private Customer customer;
    private List<SubOrder> subOrders;
    private OffsetDateTime createdAt;
    private String status;

    public Order(Integer id, Event event,Customer customer, List<SubOrder> subOrders, OffsetDateTime createdAt, String status) {
        this.id = id;
        this.event = event;
        this.customer = customer;
        this.subOrders = subOrders;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Order(Event event,Customer customer) {
        this.event = event;
        this.customer = customer;
        this.createdAt = OffsetDateTime.now();
        this.status = "Ordered";
    }

    public Event loadEvent() {
        if (event == null)
            load();
        return event;
    }

    public Customer loadCustomer() {
        if (customer == null)
            load();
        return customer;
    }

    public List<SubOrder> loadSubOrders() {
        if (subOrders == null)
            load();
        return subOrders;
    }

    public OffsetDateTime loadCreatedAt() {
        if (createdAt == null)
            load();
        return createdAt;
    }

    public String loadStatus() {
        if (status == null)
            load();
        return status;
    }

    private void load() {
        logger.info("Loading Order [id=" + id + "]");
        Order order;
        try {
            order = OrderMapper.loadById(id);
            assert order != null;
            event = order.loadEvent();
            customer = order.loadCustomer();
            subOrders = order.loadSubOrders();
            createdAt = order.loadCreatedAt();
            status = order.loadStatus();
        } catch (SQLException e) {
            logger.error(String.format("Error loading Order [id=%d]: %s", id, e.getMessage()));
        }
    }
}
