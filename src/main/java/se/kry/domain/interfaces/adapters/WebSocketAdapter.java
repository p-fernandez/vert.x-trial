package se.kry.domain.interfaces.adapters;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import se.kry.domain.entities.Service;

import java.util.List;

public class WebSocketAdapter {
    private static final String TYPE_MESSAGE = "updatedServices";
    private static final String KEY_TYPE_MESSAGE = "type";
    private static final String KEY_SERVICES = "services";

    public static String buildMessageFromServices(List<Service> services) {
        JsonArray list = new JsonArray();
        for(Service service : services) {
            JsonObject obj = service.toJsonObject();
            list.add(obj);
        }

        JsonObject data = new JsonObject();
        data.put(KEY_TYPE_MESSAGE, TYPE_MESSAGE);
        data.put(KEY_SERVICES, list);
        return Json.encode(data);
    }
}
