package edu.unimelb.swen90007.mes.constants;

public class Constant {
    public static final String API_PREFIX = "/api";
    // JWT
    public static final String JWT_ISSUER = "edu.unimelb.swen90007.mes";
    public static final long JWT_EXPIRY = 36000L;
    public static final String JWT_AUTHORITIES_CLAIM = "authority";
    public static final String JWT_USER_ID_CLAIM = "userId";
    // Event status
    public static final Integer EVENT_IN_SIX = 1;
    public static final Integer EVENT_OUT_SIX = 2;
    public static final Integer EVENT_PAST = 3;
    public static final Integer EVENT_CANCELLED = 4;
    // Order status
    public static final String ORDER_SUCCESS = "Success";
    public static final String ORDER_CANCELLED = "Cancelled";
}
