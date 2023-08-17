package edu.unimelb.swen90007.mes.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.unimelb.swen90007.mes.datamapper.DBConnection;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "RegisterCustomerServlet", value = "/register")
public class RegisterCustomerServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RegisterCustomerServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Test</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestString = IOUtils.toString(request.getReader());
        JSONObject requestData = JSON.parseObject(requestString);
        JSONObject responseData = new JSONObject();

        DBConnection db = new DBConnection();
        String sql = "INSERT INTO users (email, password, first_name, last_name, type)\n" +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, requestData.getString("email"));
            ps.setString(2, requestData.getString("password"));
            ps.setString(3, requestData.getString("firstName"));
            ps.setString(4, requestData.getString("lastName"));
            ps.setInt(5, requestData.getInteger("type"));
            ps.executeUpdate();
            responseData.put("message", "success");
            response.setStatus(200);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            responseData.put("message", e.getMessage());
            response.setStatus(500);

        } finally {
            db.closeConnection();
        }

        response.setContentType("application/json");
        response.getWriter().write(responseData.toString());
    }
}
