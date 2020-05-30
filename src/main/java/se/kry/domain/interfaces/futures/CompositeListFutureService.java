package se.kry.domain.interfaces.futures;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.impl.CompositeFutureImpl;
import se.kry.domain.entities.Service;

import java.util.List;

public interface CompositeListFutureService extends CompositeFuture {

    static CompositeFuture all(List<Future<Service>> futures) {
        return CompositeFutureImpl.all(futures.toArray(new Future[futures.size()]));
    }
}
