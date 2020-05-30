package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.entities.Service;
import se.kry.domain.interfaces.futures.CompositeListFutureService;

import java.util.*;

public class UpdateMultipleServices {
    private final UpdateService updateService;

    public UpdateMultipleServices(Vertx vertx) {
        updateService = new UpdateService(vertx);
    }

    public Future<List<Service>> execute(List<Service> services) {
        Future<List<Service>> deliveredServices = Future.future();

        List<Future<Service>> updatedServices = new ArrayList<>();

        for(Service service : services) {
            Future<Service> future = Future.future();

            updatedServices.add(
                updateService.execute(service.getId().toString(), service.toJsonObject()).setHandler(res -> {
                    if (res.failed()) {
                        future.complete(service);
                    } else {
                        Service updatedService = res.result();
                        future.complete(updatedService);
                    }
                })
            );
        }

        CompositeListFutureService.all(updatedServices).setHandler(res -> {
            List<Service> result = res.result().list();

            deliveredServices.complete(result);
        });

        return deliveredServices;
    }
}
