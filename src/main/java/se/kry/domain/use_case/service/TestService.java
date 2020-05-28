package se.kry.domain.use_case.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.entity.Service;

import java.util.ArrayList;
import java.util.List;

public class TestService {

    public TestService(Vertx vertx) {

    }

    public Future<List<String>> execute() {
        Future<List<String>> service = Future.future();

        List<String> list = new ArrayList<>();
        list.add("TEST");
        list.get(2); // Uncomment / comment line to make this use case to fail or not.

        service.complete(list);

        return service;
    }
}