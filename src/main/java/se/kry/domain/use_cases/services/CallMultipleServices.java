package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.entities.Service;
import se.kry.domain.interfaces.futures.CompositeListFutureService;

import java.util.*;

public class CallMultipleServices {
    private final CallService callService;

    public CallMultipleServices(Vertx vertx) {
        callService = new CallService(vertx);
    }

    public Future<List<Service>> execute(List<Service> services) {
        Future<List<Service>> deliveredServices = Future.future();

        List<Future<Service>> calledServices = new ArrayList<>();

        for (Service service : services) {
            Future<Service> future = Future.future();

            calledServices.add(
                callService.execute(service).setHandler(res -> {
                    if (res.failed()) {
                        future.complete(service);
                    } else {
                        Service result = res.result();
                        future.complete(result);
                    }
                })
            );
        }

        CompositeListFutureService.all(calledServices).setHandler(res -> {
            List<Service> result = res.result().list();
            deliveredServices.complete(result);
        });

        return deliveredServices;
    }
}
