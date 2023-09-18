package edu.unimelb.swen90007.mes.servlet;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.Venue;
import edu.unimelb.swen90007.mes.service.IPublicService;
import edu.unimelb.swen90007.mes.service.impl.PublicService;
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

@WebServlet(name = "PublicVenueServlet", urlPatterns = Constant.API_PREFIX + "/public/venues/*")
public class PublicVenueServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PublicVenueServlet.class);

    /**
     * GET
     * [/public/venues] view all venues
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IPublicService publicService = new PublicService();
        try {
            List<Venue> venues = publicService.viewAllVenues();
            ResponseWriter.write(response, 200, "Success", venues);
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

}
