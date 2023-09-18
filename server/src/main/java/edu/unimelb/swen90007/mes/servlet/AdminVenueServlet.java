package edu.unimelb.swen90007.mes.servlet;

import com.alibaba.fastjson.JSONObject;
import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.Venue;
import edu.unimelb.swen90007.mes.service.IAdminService;
import edu.unimelb.swen90007.mes.service.impl.AdminService;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AdminVenueServlet", urlPatterns = Constant.API_PREFIX + "/admin/venues")
public class AdminVenueServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AdminVenueServlet.class);

    /**
     * POST
     * [/admin/venues] create a venue
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = IOUtils.toString(request.getReader());
        Venue venue = JSONObject.parseObject(requestBody, Venue.class);
        IAdminService adminService = new AdminService();
        try {
            adminService.createVenue(venue);
            ResponseWriter.write(response, 201, "Success");
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

}
