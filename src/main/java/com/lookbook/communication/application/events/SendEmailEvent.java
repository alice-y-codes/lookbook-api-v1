package com.lookbook.communication.application.events;

import java.util.Map;

import lombok.Getter;

/**
 * Base application event for sending emails.
 * Contains common email sending parameters.
 */
@Getter
public class SendEmailEvent {
    private final String to;
    private final String subject;
    private final String template;
    private final Map<String, Object> templateData;

    public SendEmailEvent(String to, String subject, String template, Map<String, Object> templateData) {
        this.to = to;
        this.subject = subject;
        this.template = template;
        this.templateData = templateData;
    }
}