package com.lookbook.communication.infrastructure.events;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.lookbook.communication.application.events.SendEmailEvent;
import com.lookbook.communication.infrastructure.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener for email-related events.
 * Handles sending emails using the EmailService.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener {
    private final EmailService emailService;

    /**
     * Handles email sending events after successful transaction commit.
     *
     * @param event The email event to handle
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailEvent(SendEmailEvent event) {
        log.info("Sending email to: {} using template: {}", event.getTo(), event.getTemplate());
        try {
            emailService.sendEmail(
                    event.getTo(),
                    event.getSubject(),
                    event.getTemplate(),
                    event.getTemplateData());
            log.info("Email sent successfully to: {}", event.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", event.getTo(), e);
            throw e; // Re-throw to trigger transaction rollback
        }
    }
}