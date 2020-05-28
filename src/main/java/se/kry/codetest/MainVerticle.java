package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import se.kry.domain.interfaces.exception.NotFoundException;
import se.kry.domain.use_case.service.*;

import java.util.HashMap;

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
        router.route("/*").handler(StaticHandler.create())
            .failureHandler(routingContext -> apiErrorHandler(routingContext));

        router.get("/service").handler(routingContext -> {
            GetAllServices getAllServices = new GetAllServices(this.vertx);
            getAllServices.execute().setHandler(result -> successHandler(routingContext, result));
        });

        router.delete("/service/:id").handler(routingContext -> {
            String id = routingContext.request().getParam("id");
            RemoveService removeService = new RemoveService(this.vertx);
            removeService.execute(id).setHandler(result -> successHandler(routingContext, result, 204));
        });

        router.get("/service/:id").handler(routingContext -> {
            String id = routingContext.request().getParam("id");
            GetService getService = new GetService(this.vertx);
            getService.execute(id).setHandler(result -> successHandler(routingContext, result));
        });

        router.get("/test").handler(routingContext -> {
            TestService testService = new TestService(this.vertx);
            testService.execute().setHandler(result -> successHandler(routingContext, result));
        });

        router.post("/service").handler(routingContext -> {
            JsonObject jsonBody = routingContext.getBodyAsJson();
            CreateService createService = new CreateService(this.vertx);
            createService.execute(jsonBody).setHandler(result -> successHandler(routingContext, result, 201));
        });
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

    private void serviceUnavailableResponse(RoutingContext routingContext, Throwable ex) {
        routingContext.response().setStatusCode(503)
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
            .end(new Json().encode(result.result()));
    }

    private void successHandler(RoutingContext routingContext, AsyncResult<?> result, Integer statusCode) {
        if (result.failed()) {
            routingContext.fail(result.cause());
            return;
        }

        routingContext.response()
            .setStatusCode(statusCode)
            .putHeader("content-type", "application/json")
            .end(new Json().encode(result.result()));
    }

    private void apiErrorHandler(RoutingContext routingContext) {
        Throwable failure = routingContext.failure();
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



