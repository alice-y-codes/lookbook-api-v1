package com.lookbook.communication.infrastructure.services;

import java.util.Map;

/**
 * Service interface for sending emails.
 */
public interface EmailService {
    /**
     * Sends an email using the specified template and data.
     *
     * @param to           The recipient's email address
     * @param subject      The email subject
     * @param template     The template name to use
     * @param templateData The data to populate the template
     */
    void sendEmail(String to, String subject, String template, Map<String, Object> templateData);
}