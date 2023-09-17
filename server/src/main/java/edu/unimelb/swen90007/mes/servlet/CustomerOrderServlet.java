package edu.unimelb.swen90007.mes.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.ICustomerService;
import edu.unimelb.swen90007.mes.service.impl.CustomerService;
import edu.unimelb.swen90007.mes.util.JwtUtil;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CustomerOrderServlet", urlPatterns = Constant.API_PREFIX + "/customer/orders/*")
public class CustomerOrderServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(CustomerOrderServlet.class);

    /**
     * GET
     * [/customer/orders] view all my orders
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ICustomerService customerService = new CustomerService();
        try {
            // get customer id
            Integer customerId = JwtUtil.getInstance().getUserId(request);
            List<Order> orders = customerService.viewOwnOrder(new Customer(customerId));
            ResponseWriter.write(response, 200, "Success", orders);
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

    /**
     * POST
     * [/customer/orders] place an order
     * [/customer/orders/cancel/{id}] cancel an order
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getPathInfo();
        if (requestPath == null) {
            placeOrder(request, response);
        } else {
            String[] pathStrings = requestPath.split("/");
            if (pathStrings[1].equalsIgnoreCase("cancel")) {
                int orderId;
                try {
                    orderId = Integer.parseInt(pathStrings[2]);
                    cancelOrder(response, orderId);
                } catch (NumberFormatException e) {
                    logger.error("Invalid orderId format");
                }
            } else {
                ResponseWriter.write(response, 400, "Bad request");
            }
        }
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // deserialize json request body into Order object
        String requestBody = IOUtils.toString(request.getReader());
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        Integer eventId = requestJson.getInteger("eventId");
        JSONArray subOrdersJson = requestJson.getJSONArray("subOrders");
        List<SubOrder> subOrders = new ArrayList<>();

        for (int i = 0; i < subOrdersJson.size(); i++) {
            JSONObject subOrderJson = subOrdersJson.getJSONObject(i);
            Integer sectionId = subOrderJson.getInteger("sectionId");
            Integer quantity = subOrderJson.getInteger("quantity");
            BigDecimal unitPrice = subOrderJson.getBigDecimal("unitPrice");
            String currency = subOrderJson.getString("currency");
            SubOrder subOrder = new SubOrder(
                    new Section(sectionId), quantity, new Money(unitPrice, currency));
            subOrders.add(subOrder);
        }

        Integer customerId = JwtUtil.getInstance().getUserId(request);
        Order order = new Order(new Event(eventId), new Customer(customerId), subOrders);

        // call service
        ICustomerService customerService = new CustomerService();
        try {
            customerService.placeOrder(order);
            ResponseWriter.write(response, 201, "Success");
        } catch (Exception e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

    private void cancelOrder(HttpServletResponse response, Integer orderId) throws IOException {
        ICustomerService customerService = new CustomerService();
        try {
            Order order = new Order(orderId);
            customerService.cancelOrder(order);
            ResponseWriter.write(response, 200, "Success");
        } catch (Exception e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
        }
    }

}
