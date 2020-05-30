package se.kry.repository.web;

public class SuccessfulResponse {
    private final Integer statusCode;

    public SuccessfulResponse(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }
}