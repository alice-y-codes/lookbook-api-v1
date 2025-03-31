package com.lookbook.communication.application.events;

import java.util.Map;

/**
 * Application event for sending password reset emails to users.
 */
public class SendPasswordResetEmailEvent extends SendEmailEvent {

    public SendPasswordResetEmailEvent(String email, String resetToken) {
        super(
                email,
                "Reset Your Password",
                "password-reset-email",
                Map.of("resetToken", resetToken));
    }
}