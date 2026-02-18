package org.usermanagement.usermanagement.constant;

public final class ApiConstants {

    public static final String API_VERSION = "/v/api";

    public static final String TEST = "/test";

    public static final class User{
        public static final String USER_BASE_API=API_VERSION+"/users";
        public static final String CREATE_USER_API="";
        public static final String GET_USER="/{userId}";
        public static final String GET_USERS="";
        public static final String UPDATE_USER_API="/{userId}";
        public static final String DELETE_USER_API="/{userId}";

    }
}
