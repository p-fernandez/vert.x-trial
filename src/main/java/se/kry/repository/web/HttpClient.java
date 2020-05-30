package se.kry.repository.web;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class HttpClient {
    private static WebClient client;

    public HttpClient(Vertx vertx) {
        client = WebClient.create(vertx);
    }

    public Future<SuccessfulResponse> get(String uri, Integer milliseconds) {
        Future<SuccessfulResponse> request = Future.future();

        client.getAbs(uri)
            .timeout(milliseconds)
            .send(res -> {
                if (res.failed()) {
                    request.fail(res.cause());
                } else {
                    HttpResponse<Buffer> response = res.result();
                    Integer statusCode = response.statusCode();
                    SuccessfulResponse result = new SuccessfulResponse(statusCode);
                    request.complete(result);
                }
            });

        return request;
    }
}