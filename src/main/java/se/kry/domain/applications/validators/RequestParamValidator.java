package se.kry.domain.applications.validators;


import se.kry.domain.interfaces.exceptions.ValidatorException;

public class RequestParamValidator {
    public RequestParamValidator() {}

    public void validateId(String id) throws ValidatorException {
        try {
            Integer.parseInt(id);
        } catch (Exception e) {
           throw new ValidatorException(id + " is not a valid 'id'");
        }
    }

}
