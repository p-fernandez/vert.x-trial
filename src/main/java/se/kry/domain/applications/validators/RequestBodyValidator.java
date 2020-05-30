package se.kry.domain.applications.validators;

import io.vertx.core.json.JsonObject;
import se.kry.domain.interfaces.exceptions.ValidatorException;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestBodyValidator {
    public RequestBodyValidator() {}

    public void validateBody(JsonObject json) throws ValidatorException {
        try {
            String url = json.getString("url");

            URL obj = new URL(url);
            String protocol = obj.getProtocol().toLowerCase();
            if (!protocol.equals("http") && !protocol.equals("https")) {
                throw new ValidatorException("Wrong protocol.");
            }

            Pattern hostPattern = Pattern.compile("^(www.)?([a-zA-Z0-9]+.)?[a-zA-Z0-9]*\\.[a-z]{2,3}(\\.[a-z]+)?$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = hostPattern.matcher(obj.getHost());
            Boolean matches = matcher.matches();
            if (!matches) {
                throw new ValidatorException("Wrong hostname: " + obj.getHost());
            }
        } catch (Exception e) {
            throw new ValidatorException("Not a valid URL. " + e.getMessage());
        }
    }

}
