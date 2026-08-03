package com.dbtraining.reconx.audit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "recon.audit")
public class AuditProperties {

    /**
     * Enables or disables audit logging.
     */
    private boolean enabled = true;

    /**
     * Audit destination (console, kafka, db, etc.).
     */
    private String destination = "console";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}