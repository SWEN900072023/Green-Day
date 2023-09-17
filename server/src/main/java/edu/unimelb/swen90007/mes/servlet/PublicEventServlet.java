package edu.unimelb.swen90007.mes.servlet;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.Event;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PublicEventServlet", urlPatterns = Constant.API_PREFIX + "/public/events/*")
public class PublicEventServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PublicEventServlet.class);

    /**
     * GET
     * [/public/events] view all events
     * [/public/events/six-months] view upcoming events in six months
     * [/public/events?title=green%20day] search by title
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getPathInfo();
        String requestPathName = (requestPath == null) ? "" : requestPath.split("/")[1];

        List<Event> events = new ArrayList<>();
        IPublicService publicService = new PublicService();

        try {
            if (requestPathName.equalsIgnoreCase("six-months")) {
                events = publicService.viewNextSixMonthsEvents();

            } else if (requestPathName.isEmpty()) {
                String title = request.getParameter("title");
                if (title != null) {
                    events = publicService.searchEvents(title);
                } else {
                    events = publicService.viewAllEvents();
                }
            }

            ResponseWriter.write(response, 200, "Success", events);

        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

}
