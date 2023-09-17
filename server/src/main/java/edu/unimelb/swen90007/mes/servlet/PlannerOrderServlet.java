package edu.unimelb.swen90007.mes.servlet;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.service.IEventPlannerService;
import edu.unimelb.swen90007.mes.service.impl.EventPlannerService;
import edu.unimelb.swen90007.mes.util.JwtUtil;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PlannerOrderServlet", urlPatterns = Constant.API_PREFIX + "/planner/orders/*")
public class PlannerOrderServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PlannerOrderServlet.class);

    /**
     * GET
     * [/planner/orders?eventId=1] view orders of a hosted event
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String eventIdString = request.getParameter("eventId");
        IEventPlannerService eventPlannerService = new EventPlannerService();

        if (eventIdString != null) {
            try {
                Integer eventId = Integer.parseInt(eventIdString);
                // get user id
                Integer userId = JwtUtil.getInstance().getUserId(request);
                EventPlanner eventPlanner = new EventPlanner(userId);
                Event event = new Event(eventId);
                List<Order> orders = eventPlannerService.viewOrders(eventPlanner, event);
                ResponseWriter.write(response, 200, "Success", orders);

            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    ResponseWriter.write(response, 400, "Invalid eventId format");
                } else {
                    ResponseWriter.write(response, 500, "Unexpected system error");
                }
                logger.error(e.getMessage());
            }
        } else {
            ResponseWriter.write(response, 400, "Bad request");
        }
    }

}
