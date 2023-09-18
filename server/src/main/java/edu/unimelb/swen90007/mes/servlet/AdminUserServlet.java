package edu.unimelb.swen90007.mes.servlet;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.service.IAdminService;
import edu.unimelb.swen90007.mes.service.impl.AdminService;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AdminUserServlet", urlPatterns = Constant.API_PREFIX + "/admin/users")
public class AdminUserServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AdminUserServlet.class);

    /**
     * GET
     * [/admin/users] view all users
     * [/admin/users?type=Customer] view all customers
     * [/admin/users?type=EventPlanner] view all customers
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userType = request.getParameter("type");
        if (userType == null) userType = "";
        IAdminService adminService = new AdminService();
        try {
            if (userType.equalsIgnoreCase(Customer.class.getSimpleName())) {
                List<AppUser> customers = adminService.viewAllCustomers();
                ResponseWriter.write(response, 200, "Success", customers);
            } else if (userType.equalsIgnoreCase(EventPlanner.class.getSimpleName())) {
                List<AppUser> eventPlanners = adminService.viewAllEventPlanners();
                ResponseWriter.write(response, 200, "Success", eventPlanners);
            } else {
                List<AppUser> users = adminService.viewAllUsers();
                ResponseWriter.write(response, 200, "Success", users);
            }
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

}
