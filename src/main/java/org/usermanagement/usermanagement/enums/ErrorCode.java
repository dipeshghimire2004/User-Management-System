package org.usermanagement.usermanagement.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    RESOURCE_NOT_FOUND(404, "The requested resource was not found."),
    DUPLICATE_ENTITY(409, "Entity already exists."),
    INVALID_CREDENTIALS(401, "Invalid username or password."),
    BAD_REQUEST(400, "Invalid request parameters."),
    INTERNAL_SERVER_ERROR(500, "Something went wrong."),
    OAUTH_ACCOUNT_LINKED_EXCEPTION(401, "This account is already linked to oauth provider so this service is not available."),
    UNAUTHORIZED(403, "Access denied."),
    SAME_USER_MESSAGE_SEND(403, "You are not allowed to send a message to the same user."),
    REFRESH_TOKEN_EXPIRE(401, "Refresh token expired."),
    UNABLE_TO_PARSE_JWT(403, "Unable to parse jwt token."),
    REVIEW_DUPLICATE(400, "The requested resource was duplicated."),
    SECURITY_ERROR(400, "Not Allowed."),
    USER_AGENT_EXCEPTION(403,"The user agent is missing or empty.");

    private final int httpStatus;
    private final String message;

}