package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class OrderMapper {
    private static final Logger logger = LogManager.getLogger(OrderMapper.class);

    public static void create(Order order, Connection connection) throws SQLException {
        String sql = "INSERT INTO orders (event_id, customer_id, created_at, status) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, order.getEvent().getId());
        preparedStatement.setInt(2, order.getCustomer().getId());
        preparedStatement.setObject(3, order.getCreatedAt());
        preparedStatement.setString(4, order.getStatus());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            order.setId(generatedKeys.getInt("id"));
        }
        logger.info("New Order Created [id=" + order.getId() + "]");
    }

    public static List<Order> loadByEventId(int eventId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE event_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static List<Order> loadByCustomerID(int customerID) throws SQLException {
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, customerID);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static Order loadById(int id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet).get(0);
    }

    private static List<Order> load(ResultSet resultSet) throws SQLException {
        List<Order> orders = new ArrayList<>();

        while (resultSet.next()) {
            int orderID = resultSet.getInt("id");
            int eventID = resultSet.getInt("event_id");
            int customerID = resultSet.getInt("customer_id");
            LocalDateTime createdAt = resultSet.getObject("created_at", LocalDateTime.class);
            String status = resultSet.getString("status").trim();

            Customer customer = (Customer) AppUserMapper.loadByIdPartial(customerID);
            Event event = EventMapper.loadByIdPartial(eventID);
            List<SubOrder> subOrders = SubOrderMapper.loadByOrderId(orderID);

            Order order = new Order(orderID, event, customer, subOrders, createdAt, status);
            orders.add(order);
            logger.info("Order Loaded [id=" + orderID + "]");
        }

        return orders;
    }

    public static void cancel(Order order, Connection connection) throws SQLException {
        String sqlSelect = "SELECT status FROM orders WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
        preparedStatement.setInt(1, order.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String status = resultSet.getString("status").trim();
        if (status.equals(Constant.ORDER_SUCCESS)) {
            String sqlUpdate = "UPDATE orders SET status = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, Constant.ORDER_CANCELLED);
            preparedStatement.setInt(2, order.getId());
            preparedStatement.executeUpdate();

            logger.info("Existing Order Cancelled [id=" + order.getId() + "]");
        }
    }

    public static void delete(Order order, Connection connection) throws SQLException {
        int id = order.getId();
        SubOrderMapper.deleteByOrderId(id);
        String sql = "DELETE FROM orders WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing Order Deleted [id=" + id + "]");
    }

}
