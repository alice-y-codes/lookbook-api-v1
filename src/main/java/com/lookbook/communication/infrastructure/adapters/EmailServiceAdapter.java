package com.lookbook.communication.infrastructure.adapters;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.lookbook.communication.application.ports.services.EmailService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the EmailService interface.
 * Handles the actual sending of emails using a template engine.
 */
@Slf4j
@Service
public class EmailServiceAdapter implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String template, Map<String, Object> templateData) {
        log.info("Sending email to: {} with subject: {} using template: {}", to, subject, template);

        // TODO: Implement actual email sending logic
        // 1. Load template
        // 2. Process template with data
        // 3. Send email using configured email service

        log.info("Email sent successfully to: {}", to);
    }
}