package se.kry.repository.web;

import io.vertx.core.json.JsonObject;

public class SuccessfulResponse {
    private Integer statusCode;
    private JsonObject body;

    public SuccessfulResponse(Integer statusCode, JsonObject body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public JsonObject getBody() {
        return this.body;
    }

    public void setBody(JsonObject body) {
        this.body = body;
    }
}