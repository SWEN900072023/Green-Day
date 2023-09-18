package edu.unimelb.swen90007.mes.servlet;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.exceptions.PermissionDeniedException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.EventPlanner;
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
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PlannerInviteServlet", urlPatterns = Constant.API_PREFIX + "/planner/invite/*")
public class PlannerInviteServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PlannerInviteServlet.class);

    /**
     * GET
     * [/planner/invite/{eventId}] view all uninvited event planners
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getPathInfo();
        if (requestPath == null) {
            ResponseWriter.write(response, 400, "Bad request");
            return;
        }

        String eventIdString = requestPath.split("/")[1];
        try {
            Integer eventId = Integer.parseInt(eventIdString);
            IEventPlannerService eventPlannerService = new EventPlannerService();
            List<AppUser> eventPlanners = eventPlannerService.viewUninvitedEventPlanner(new Event(eventId));
            ResponseWriter.write(response, 200, "Success", eventPlanners);

        } catch (NumberFormatException e) {
            ResponseWriter.write(response, 400, "Invalid eventId format");
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

    /**
     * POST
     * [/planner/invite/{inviteeId}/{eventId}] invite another event
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getPathInfo();
        if (requestPath == null) {
            ResponseWriter.write(response, 400, "Bad request");
            return;
        }

        String[] pathStrings = requestPath.split("/");
        if (pathStrings.length != 3) {
            ResponseWriter.write(response, 400, "Bad request");
        }
        String inviteeIdString = pathStrings[1];
        String eventIdString = pathStrings[2];

        try {
            Integer inviterId = JwtUtil.getInstance().getUserId(request);
            Integer inviteeId = Integer.parseInt(inviteeIdString);
            Integer eventId = Integer.parseInt(eventIdString);
            IEventPlannerService eventPlannerService = new EventPlannerService();
            eventPlannerService.inviteEventPlanner(
                    new EventPlanner(inviterId), new EventPlanner(inviteeId), new Event(eventId));
            ResponseWriter.write(response, 200, "Success");

        } catch (NumberFormatException e) {
            ResponseWriter.write(response, 400, "Invalid path input");
        } catch (PermissionDeniedException e) {
            ResponseWriter.write(response, 403, e.getMessage());
        } catch (SQLException e) {
            ResponseWriter.write(response, 500, "Unexpected system error");
            logger.error(e.getMessage());
        }
    }

}
