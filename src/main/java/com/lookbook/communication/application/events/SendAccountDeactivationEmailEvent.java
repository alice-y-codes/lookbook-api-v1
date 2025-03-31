package com.lookbook.communication.application.events;

import java.util.Map;

/**
 * Application event for sending account deactivation emails to users.
 */
public class SendAccountDeactivationEmailEvent extends SendEmailEvent {

    public SendAccountDeactivationEmailEvent(String email, String username) {
        super(
                email,
                "Account Deactivated",
                "account-deactivation-email",
                Map.of("username", username));
    }
}