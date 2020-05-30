package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

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