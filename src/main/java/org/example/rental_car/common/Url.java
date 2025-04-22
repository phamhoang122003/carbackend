package org.example.rental_car.common;

public class Url {
    public static final String API = "/api/v1";

    public static final String USER = API + "/user";

    public static final String LOGIN = "/login";

    public static final String REGISTER = "/register";

    public static final String AUTH = API + "/auth";

    public static final String CAR = API + "/car";

    public static final String ADD = "/add";

    public static final String GET_BY_OWNER_ID = "/owner/get/{ownerId}";

    public static final String UPDATE_IMAGE_BY_ID = "/update/image/{carId}";

    public static final String REVIEW = API + "/review";

    public static final String GET_BY_CUSTOMER_ID = "/get-review/{customerId}";

    public static final String GET_BY_CAR_ID = "/get-review/{carId}";



    public static final String BOOKING = API + "/booking";

    public static final String BOOKING_APPROVED = "/{bookingId}/approve";

    public static final String BOOKING_CANCELED = "/{bookingId}/cancel";

    public static final String BOOKING_COMPLETED = "/{bookingId}/complete";

    public static final String BOOKING_REJECTED = "/{bookingId}/reject";

    public static final String GET_BY_ID = "/get/{id}";

    public static final String GET_ALL = "/get/all";

    public static final String GET_BY_EMAIL = "/get/email/{email}";

    public static final String UPDATE_BY_ID = "/update/{id}";

    public static final String DELETE_BY_ID = "/delete//{id}";

    public static final String CHANGE_PASSWORD = "/change-password/{userId}";

    public static final String TOKEN_VERIFICATION = API + "/verification";
    public static final String VALIDATE_TOKEN = "/validate-token";
    public static final String CHECK_TOKEN_EXPIRATION = "/check-token-expiration";
    public static final String SAVE_TOKEN = "/user/save-token";
    public static final String GENERATE_NEW_TOKEN_FOR_USER = "/generate-new-token";
    public static final String DELETE_TOKEN = "/delete-token";
    public static final String VERIFY_EMAIL = "/verify-your-email";
    public static final String RESEND_VERIFICATION_TOKEN = "/resend-verification-token";

    public static final String REQUEST_PASSWORD_RESET = "/request-password-reset" ;
    public static final String RESET_PASSWORD = "/reset-password" ;
}
