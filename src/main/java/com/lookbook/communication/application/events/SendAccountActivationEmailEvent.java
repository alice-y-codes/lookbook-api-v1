package com.lookbook.communication.application.events;

import java.util.Map;

/**
 * Application event for sending account activation emails to users.
 */
public class SendAccountActivationEmailEvent extends SendEmailEvent {

    public SendAccountActivationEmailEvent(String email, String activationToken) {
        super(
                email,
                "Activate Your Account",
                "account-activation-email",
                Map.of("activationToken", activationToken));
    }
}