package se.kry.domain.config.services;

public enum ServiceStatus {
    FAIL("FAIL"),
    OK("OK"),
    UNKNOWN("UNKNOWN");

    public final String status;

    ServiceStatus(String s) {
        status = s;
    }
}
