package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import se.kry.domain.use_case.service.CreateService;
import se.kry.domain.use_case.service.GetAllServices;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

    private HashMap<String, String> services = new HashMap<>();
    //TODO use this
    private BackgroundPoller poller = new BackgroundPoller();

    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        services.put("https://www.kry.se", "UNKNOWN");
        vertx.setPeriodic(1000 * 60, timerId -> poller.pollServices(services));
        setRoutes(router);
        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        System.out.println("KRY code test service started");
                        startFuture.complete();
                    } else {
                        startFuture.fail(result.cause());
                    }
                });
    }

    private void setRoutes(Router router) {
        router.route("/*").handler(StaticHandler.create());

        router.get("/service").handler(req -> {
            List<JsonObject> jsonServices = services
            .entrySet().stream()
              .map(service -> new JsonObject()
               .put("name", service.getKey())
               .put("status", service.getValue()))
                .collect(Collectors.toList());
            req.response().putHeader("content-type", "application/json")
            .end(new JsonArray(jsonServices).encode());
        });

        router.get("/services").handler(req -> {
            try {
                GetAllServices getAllServices = new GetAllServices(this.vertx);
                getAllServices.execute().setHandler(resultHandler(req, res -> req.response()
                        .putHeader("content-type", "application/json")
                        .end(new JsonArray(res).encode())));
            } catch (Exception ex) {
                internalServerError(req, ex);
            }
        });

        router.post("/service").handler(req -> {
            try {
                JsonObject jsonBody = req.getBodyAsJson();
                CreateService createService = new CreateService(this.vertx);
                createService.execute(jsonBody).setHandler(resultHandler(req, res -> req.response()
                        .putHeader("content-type", "application/json")
                        .end(new Json().encode(res))));
            } catch (Exception ex) {
                internalServerError(req, ex);
            }
        });
    }

    private <T> Handler<AsyncResult<T>> resultHandler(RoutingContext req, Handler<T> handler) {
        return res -> {
            if (res.succeeded()) {
                handler.handle(res.result());
            } else {
                serviceUnavailable(req, res.cause());
            }
        };
    }

    private void internalServerError(RoutingContext req, Throwable ex) {
        req.response().setStatusCode(500)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }

    private void serviceUnavailable(RoutingContext req, Throwable ex) {
        req.response().setStatusCode(503)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }
}



