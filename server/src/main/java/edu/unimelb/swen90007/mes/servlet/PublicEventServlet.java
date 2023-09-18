package edu.unimelb.swen90007.mes.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.List;

@WebServlet(name = "PublicEventServlet", urlPatterns = Constant.API_PREFIX + "/public/events/*")
public class PublicEventServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PublicEventServlet.class);

    /**
     * GET
     * [/public/events] view all events
     * [/public/events/six-months] view upcoming events in six months
     * [/public/events?title=green%20day] search by title
     * [/public/events/{id}] view event detail
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getPathInfo();
        String requestPathName = (requestPath == null) ? "" : requestPath.split("/")[1];

        if (requestPathName.equalsIgnoreCase("six-months")) {
            getSixMonthsEvents(response);
        } else if (requestPathName.isEmpty()) {
            String title = request.getParameter("title");
            getEventList(response, title);
        } else {
            try {
                Integer eventId = Integer.parseInt(requestPathName);
                getEventDetail(response, eventId);
            } catch (NumberFormatException e) {
                ResponseWriter.write(response, 400, "Invalid id format");
            }
        }
    }

    private void getSixMonthsEvents(HttpServletResponse response) throws IOException {
        IPublicService publicService = new PublicService();
        try {
            List<Event> events = publicService.viewNextSixMonthsEvents();
            ResponseWriter.write(response, 200, "Success", events);
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

    private void getEventList(HttpServletResponse response, String title) throws IOException {
        IPublicService publicService = new PublicService();
        try {
            List<Event> events;
            if (title != null) {
                events = publicService.searchEvents(title);
            } else {
                events = publicService.viewAllEvents();
            }
            ResponseWriter.write(response, 200, "Success", events);
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

    private void getEventDetail(HttpServletResponse response, Integer eventId) throws IOException {
        IPublicService publicService = new PublicService();
        try {
            Event event = publicService.viewEventDetail(new Event(eventId));
            // serialize embedded value Money
            String eventJSonString = JSON.toJSONString(event);
            JSONObject eventJson = JSONObject.parseObject(eventJSonString);
            JSONArray sectionsJson = eventJson.getJSONArray("sections");
            for (int i = 0; i < sectionsJson.size(); i++) {
                JSONObject sectionJson = sectionsJson.getJSONObject(i);
                JSONObject moneyJson = sectionJson.getJSONObject("money");
                sectionJson.put("currency", moneyJson.get("currency"));
                sectionJson.put("unitPrice", moneyJson.get("unitPrice"));
                sectionJson.remove("money");
            }
            ResponseWriter.write(response, 200, "Success", eventJson);
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

}
