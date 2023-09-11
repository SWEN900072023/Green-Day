package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.datamapper.OrderMapper;
import edu.unimelb.swen90007.mes.util.UnitOfWork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class Order {
    private static final Logger logger = LogManager.getLogger(Order.class);
    private int id;
    private Customer customer;
    private List<SubOrder> subOrders;
    private OffsetDateTime createdAt;
    private String status;

    public Order(int id, Customer customer, List<SubOrder> subOrders, OffsetDateTime createdAt, String status) {
        this.id = id;
        this.customer = customer;
        this.subOrders = subOrders;
        this.createdAt = createdAt;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() throws SQLException {
        if (customer == null)
            load();
        return customer;
    }

    public List<SubOrder> getSubOrders() throws SQLException {
        if (subOrders == null)
            load();
        return subOrders;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() throws SQLException {
        if (status == null)
            load();
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        UnitOfWork.getInstance().registerDirty(this);
    }

    private void load() throws SQLException {
        logger.info("Loading Order [id=" + id + "]");
        Order order = OrderMapper.loadById(id);
        assert order != null;
        customer = order.getCustomer();
        subOrders = order.getSubOrders();
        createdAt = order.getCreatedAt();
        status = order.getStatus();
    }
}
