package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.model.SubOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class OrderMapper {
    private static final Logger logger = LogManager.getLogger(OrderMapper.class);

    public static void create(int customerID, Order order) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, created_at, status) VALUES (?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, customerID);
        preparedStatement.setObject(2, order.getCreatedAt());
        preparedStatement.setString(3, order.getStatus());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            order.setId(generatedKeys.getInt("id"));
        logger.info("New Order Created [id=" + order.getId() + "]");

        List<SubOrder> subOrders = order.getSubOrders();
        for (SubOrder subOrder : subOrders) {
            SubOrderMapper.create(order.getId(), subOrder);
        }
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
            int customerID = resultSet.getInt("customer_id");
            OffsetDateTime createdAt = resultSet.getObject("created_at", OffsetDateTime.class);
            String status = resultSet.getString("status").trim();

            Customer customer = new Customer(customerID);
            List<SubOrder> subOrders = SubOrderMapper.loadByOrderId(orderID);

            Order order = new Order(orderID, customer, subOrders, createdAt, status);
            orders.add(order);
            logger.info("Order Loaded [id=" + orderID + "]");
        }

        return orders;
    }

    public static void cancel(int id) throws SQLException {
        String sql = "UPDATE orders SET status = 'Cancelled' WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing Order Cancelled [id=" + id + "]");
    }

    public static void delete(int id) throws SQLException {
        SubOrderMapper.deleteByOrderId(id);

        String sql = "DELETE FROM orders WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing Order Deleted [id=" + id + "]");
    }
}
