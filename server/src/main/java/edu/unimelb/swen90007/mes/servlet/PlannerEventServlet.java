package edu.unimelb.swen90007.mes.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.exceptions.CapacityExceedsException;
import edu.unimelb.swen90007.mes.exceptions.InvalidTimeRangeException;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.exceptions.TimeConflictException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.IEventPlannerService;
import edu.unimelb.swen90007.mes.service.impl.EventPlannerService;
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
import java.util.List;

@WebServlet(name = "PlannerEventServlet", urlPatterns = Constant.API_PREFIX + "/planner/events")
public class PlannerEventServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PlannerEventServlet.class);

    /**
     * GET
     * [/planner/events] get all hosted events
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IEventPlannerService eventPlannerService = new EventPlannerService();
        try {
            // get user id
            Integer userId = JwtUtil.getInstance().getUserId(request);
            List<Event> events = eventPlannerService.viewHostedEvent(new EventPlanner(userId));
            ResponseWriter.write(response, 200, "Success", events);
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

    /**
     * POST
     * [/planner/events] create a new event
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Event event = processRequestData(request);
        // get user id
        Integer userId = JwtUtil.getInstance().getUserId(request);
        event.setFirstPlannerId(userId);

        IEventPlannerService eventPlannerService = new EventPlannerService();
        try {
            eventPlannerService.createEvent(event);
            ResponseWriter.write(response, 201, "Success");
        } catch (CapacityExceedsException | TimeConflictException | InvalidTimeRangeException e) {
            ResponseWriter.write(response, 400, e.getMessage());
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

    /**
     * PUT
     * [/planner/events] An event planner updates a hosted event
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Event event = processRequestData(request);
        Integer userId = JwtUtil.getInstance().getUserId(request);
        EventPlanner eventPlanner = new EventPlanner(userId);

        IEventPlannerService eventPlannerService = new EventPlannerService();
        try {
            eventPlannerService.modifyEvent(eventPlanner, event);
            ResponseWriter.write(response, 200, "Success");
        } catch (PermissionDeniedException e) {
            ResponseWriter.write(response, 403, e.getMessage());
        } catch (CapacityExceedsException | TimeConflictException | InvalidTimeRangeException e) {
            ResponseWriter.write(response, 400, e.getMessage());
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

    private Event processRequestData(HttpServletRequest request) throws IOException {
        String requestBody = IOUtils.toString(request.getReader());
        Event event = JSONObject.parseObject(requestBody, Event.class);
        JSONObject eventJson = JSONObject.parseObject(requestBody);

        List<Section> sections = event.getSections();
        JSONArray sectionsJson = eventJson.getJSONArray("sections");
        for (int i = 0; i < sectionsJson.size(); i++) {
            // set embedded value event.money
            JSONObject sectionJson = sectionsJson.getJSONObject(i);
            BigDecimal unitPrice = sectionJson.getBigDecimal("unitPrice");
            String currency = sectionJson.getString("currency");
            Section section = sections.get(i);
            Money money = new Money(unitPrice, currency);
            section.setMoney(money);
            // set event.remainingTickets
            if (section.getRemainingTickets() == null) {
                // create request
                section.setRemainingTickets(new SectionTickets(section.getId(), section.getCapacity()));
            }
        }

        // set venue
        Integer venueId = eventJson.getInteger("venueId");
        Venue venue = new Venue(venueId);
        event.setVenue(venue);

        return event;
    }

}
