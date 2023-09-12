package edu.unimelb.swen90007.mes.util;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseWriter {
    public static <T> void write(HttpServletResponse response,
                                      int statusCode, String msg, T data) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        JSONObject responseData = new JSONObject();
        responseData.put("message", msg);
        if (data != null) {
            responseData.put("data", data);
        }
        response.getWriter().write(responseData.toString());
    }

    public static void write(HttpServletResponse response,
                                 int statusCode, String msg) throws IOException {
        ResponseWriter.write(response, statusCode, msg, null);
    }
}
