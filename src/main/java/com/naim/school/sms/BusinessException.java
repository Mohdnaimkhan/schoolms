package com.naim.school.sms;

/**
 * Thrown for expected, user-facing validation/business errors
 * (e.g. "Username already exists.", "User not found.").
 *
 * Its message is safe to display directly to the end user. Any other
 * exception (bugs, DB errors, null pointers, etc.) is treated as
 * unexpected and is never shown to the user in detail - see
 * {@link GlobalExceptionHandler}.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
