package edu.unimelb.swen90007.mes.servlet;

import com.alibaba.fastjson.JSONObject;
import edu.unimelb.swen90007.mes.model.Customer;
import edu.unimelb.swen90007.mes.service.IAppUserService;
import edu.unimelb.swen90007.mes.service.impl.AppUserServiceImpl;
import edu.unimelb.swen90007.mes.util.ResponseWriter;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "RegisterCustomerServlet", value = "/api/register")
public class RegisterCustomerServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RegisterCustomerServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestString = IOUtils.toString(request.getReader());
        Customer customer = JSONObject.parseObject(requestString, Customer.class);

        IAppUserService userService = new AppUserServiceImpl();
        try {
            userService.register(customer);
            ResponseWriter.write(response, 201, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            ResponseWriter.write(response, 400, e.getMessage());
        }
    }
}
