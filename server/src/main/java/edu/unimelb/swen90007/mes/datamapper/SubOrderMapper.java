package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Money;
import edu.unimelb.swen90007.mes.model.Section;
import edu.unimelb.swen90007.mes.model.SubOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class SubOrderMapper {
    private static final Logger logger = LogManager.getLogger(SubOrderMapper.class);

    public static void create(int orderID, SubOrder subOrder) throws SQLException {
        String sql = "INSERT INTO order_sections (order_id, section_id, quantity, unit_price, currency) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, orderID);
        preparedStatement.setInt(2, subOrder.getSection().getID());
        preparedStatement.setInt(3, subOrder.getQuantity());
        preparedStatement.setBigDecimal(4, subOrder.getMoney().getUnitPrice());
        preparedStatement.setString(5, subOrder.getMoney().getCurrency());
        preparedStatement.executeUpdate();
        logger.info("New Association Created [order_id=" + orderID + "], [section_id=" + subOrder.getSection().getID() + "]");

        SectionMapper.decreaseRemainingTickets(subOrder.getSection().getID(), subOrder.getQuantity());
    }

    public static List<SubOrder> loadByOrderID(int orderID) throws SQLException {
        List<SubOrder> subOrders = new ArrayList<>();

        String sql = "SELECT * FROM order_sections WHERE order_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, orderID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int sectionID = resultSet.getInt("section_id");
            int quantity = resultSet.getInt("quantity");
            BigDecimal unitPrice = resultSet.getBigDecimal("unit_price");
            String currency = resultSet.getString("currency").trim();

            Section section = SectionMapper.loadByID(sectionID);
            Money money = new Money(unitPrice, currency);
            SubOrder subOrder = new SubOrder(section, quantity, money);

            subOrders.add(subOrder);
            logger.info("Association Loaded [order_id=" + orderID + "], [section_id=" + sectionID + "]");
        }

        return subOrders;
    }

    public static void deleteByOrderID(int orderID) throws SQLException {
        String sql = "DELETE FROM order_sections WHERE order_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, orderID);
        preparedStatement.executeUpdate();
        logger.info("Existing Associated Suborders Deleted [order_id=" + orderID + "]");
    }
}
