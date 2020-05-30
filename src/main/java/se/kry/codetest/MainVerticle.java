package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import se.kry.domain.config.websockets.WebSocket;
import se.kry.domain.interfaces.exceptions.NotFoundException;
import se.kry.domain.interfaces.exceptions.ValidatorException;
import se.kry.domain.use_cases.services.*;

import java.util.HashSet;
import java.util.Set;

public class MainVerticle extends AbstractVerticle {
    private BackgroundPoller poller;

    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        poller = new BackgroundPoller(vertx);
        vertx.setPeriodic(1000 * 60, timerId -> poller.pollServices());

        setRoutes(router);

        vertx
            .createHttpServer()
            .websocketHandler(this::webSocketHandler)
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

    private void webSocketHandler(ServerWebSocket handler) {
        Set<String> ids = new HashSet<>();
        final String id = handler.textHandlerID();
        System.out.println("Client connected: " + id);
        ids.add(id); // We add it to the pool of clients.

        vertx.eventBus().consumer(WebSocket.WS_PATH, message -> {
            // We are unaware if the client disconnected so we check
            // the clients pool first to avoid an UnhandledException.
            if (ids.contains(id)) {
                handler.writeTextMessage((String) message.body());
            }
        });

        handler.textMessageHandler(message -> vertx.eventBus().publish(WebSocket.WS_PATH, message));

        handler.closeHandler(message -> {
            ids.remove(id);
            System.out.println("Client disconnected: " + id);
        });
    }

    private void setRoutes(Router router) {
        router.route("/*").handler(StaticHandler.create())
            .failureHandler(this::apiErrorHandler);

        router.get("/services").handler(routingContext -> {
            GetAllServices getAllServices = new GetAllServices(this.vertx);
            getAllServices.execute().setHandler(result -> successHandler(routingContext, result));
        });

        router.delete("/services/:id").handler(routingContext -> {
            String id = routingContext.request().getParam("id");

            RemoveService removeService = new RemoveService(this.vertx);
            removeService.execute(id).setHandler(result -> successHandler(routingContext, result, 204));
        });

        router.get("/services/:id").handler(routingContext -> {
            String id = routingContext.request().getParam("id");

            GetService getService = new GetService(this.vertx);
            getService.execute(id).setHandler(result -> successHandler(routingContext, result));
        });

        router.get("/test").handler(routingContext -> {
            TestService testService = new TestService();
            testService.execute().setHandler(result -> successHandler(routingContext, result));
        });

        router.post("/services").handler(routingContext -> {
            JsonObject jsonBody = routingContext.getBodyAsJson();

            CreateService createService = new CreateService(this.vertx);
            createService.execute(jsonBody).setHandler(result -> successHandler(routingContext, result, 201));
        });

        router.put("/services/:id")
            .handler(routingContext -> {
                String id = routingContext.request().getParam("id");
                JsonObject jsonBody = routingContext.getBodyAsJson();

                UpdateService updateService = new UpdateService(this.vertx);
                updateService.execute(id, jsonBody).setHandler(result -> successHandler(routingContext, result, 200));
            });
    }

    private void badRequestResponse(RoutingContext routingContext, Throwable ex) {
        routingContext.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }

    private void internalServerErrorResponse(RoutingContext routingContext, Throwable ex) {
        routingContext.response().setStatusCode(500)
            .putHeader("content-type", "application/json")
            .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }

    private void notFoundResponse(RoutingContext routingContext, Throwable ex) {
        routingContext.response().setStatusCode(404)
            .putHeader("content-type", "application/json")
            .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }

    private void successHandler(RoutingContext routingContext, AsyncResult<?> result) {
        if (result.failed()) {
            routingContext.fail(result.cause());
            return;
        }

        routingContext.response()
            .putHeader("content-type", "application/json")
            .end(Json.encode(result.result()));
    }

    private void successHandler(RoutingContext routingContext, AsyncResult<?> result, Integer statusCode) {
        if (result.failed()) {
            routingContext.fail(result.cause());
            return;
        }

        routingContext.response()
            .setStatusCode(statusCode)
            .putHeader("content-type", "application/json")
            .end(Json.encode(result.result()));
    }

    private void apiErrorHandler(RoutingContext routingContext) {
        Throwable failure = routingContext.failure();
        if (failure instanceof ValidatorException) {
            badRequestResponse(routingContext, failure);
            return;
        }

        if (failure instanceof NotFoundException) {
            notFoundResponse(routingContext, failure);
            return;
        }

        if (failure instanceof NullPointerException) {
            internalServerErrorResponse(routingContext, new Exception("Internal server error"));
            return;
        }

        internalServerErrorResponse(routingContext, failure);
    }
}



