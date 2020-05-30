package se.kry.domain.config.service;

public enum ServiceStatus {
    FAIL("FAIL"),
    OK("OK"),
    UNKNOWN("UNKNOWN");

    public final String status;

    ServiceStatus(String s) {
        status = s;
    }
}
