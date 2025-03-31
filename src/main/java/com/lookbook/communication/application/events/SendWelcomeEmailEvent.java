package com.lookbook.communication.application.events;

import java.util.Map;

/**
 * Application event for sending welcome emails to new users.
 */
public class SendWelcomeEmailEvent extends SendEmailEvent {

    public SendWelcomeEmailEvent(String email, String username) {
        super(
                email,
                "Welcome to Lookbook!",
                "welcome-email",
                Map.of("username", username));
    }
}