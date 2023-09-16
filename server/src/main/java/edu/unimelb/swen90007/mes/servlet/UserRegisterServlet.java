package edu.unimelb.swen90007.mes.servlet;

import com.alibaba.fastjson.JSONObject;
import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.service.impl.PublicService;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "UserRegisterServlet", urlPatterns = Constant.API_PREFIX + "/register/*")
public class UserRegisterServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(UserRegisterServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getPathInfo();
        String requestBody = IOUtils.toString(request.getReader());
        String userType = requestPath.split("/")[1];

        AppUser user;
        switch (userType) {
            case "customer" -> user = JSONObject.parseObject(requestBody, Customer.class);
            case "event-planner" -> user = JSONObject.parseObject(requestBody, EventPlanner.class);
            default -> {
                ResponseWriter.write(response, 400, "Bad request");
                return;
            }
        }

        PublicService publicService = new PublicService();
        try {
            publicService.register(user);
            ResponseWriter.write(response, 201, "Success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            ResponseWriter.write(response, 400, e.getMessage());
        }
    }
}
