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

    public static void create(int OrderId, SubOrder subOrder) throws SQLException {
        String sql = "INSERT INTO order_sections (order_id, section_id, quantity, unit_price, currency) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, OrderId);
        preparedStatement.setInt(2, subOrder.getSection().getId());
        preparedStatement.setInt(3, subOrder.getQuantity());
        preparedStatement.setBigDecimal(4, subOrder.getMoney().getUnitPrice());
        preparedStatement.setString(5, subOrder.getMoney().getCurrency());
        preparedStatement.executeUpdate();
        logger.info("New Association Created [order_id=" + OrderId + "], [section_id=" + subOrder.getSection().getId() + "]");

        SectionMapper.decreaseRemainingTickets(subOrder.getSection().getId(), subOrder.getQuantity());
    }

    public static List<SubOrder> loadByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM order_sections WHERE order_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, orderId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    private static List<SubOrder> load(ResultSet resultSet) throws SQLException {
        List<SubOrder> subOrders = new ArrayList<>();

        while (resultSet.next()) {
            int orderId = resultSet.getInt("order_id");
            int sectionId = resultSet.getInt("section_id");
            int quantity = resultSet.getInt("quantity");
            BigDecimal unitPrice = resultSet.getBigDecimal("unit_price");
            String currency = resultSet.getString("currency").trim();

            Section section = new Section(sectionId);
            Money money = new Money(unitPrice, currency);
            SubOrder subOrder = new SubOrder(section, quantity, money);

            subOrders.add(subOrder);
            logger.info("Association Loaded [order_id=" + orderId + "], [section_id=" + sectionId + "]");
        }

        return subOrders;
    }

    public static void deleteByOrderId(int orderId) throws SQLException {
        String sql = "DELETE FROM order_sections WHERE order_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, orderId);
        preparedStatement.executeUpdate();
        logger.info("Existing Associated Suborders Deleted [order_id=" + orderId + "]");
    }
}
