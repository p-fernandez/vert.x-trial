package se.kry.infrastructure.interfaces.exceptions;

import se.kry.domain.interfaces.exceptions.NotFoundException;

public class DatabaseRecordNotFoundException extends NotFoundException {
  public DatabaseRecordNotFoundException(String message) {
    super(message);
  }
}
