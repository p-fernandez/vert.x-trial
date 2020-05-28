package se.kry.infrastructure.interfaces.exception;

import se.kry.domain.interfaces.exception.NotFoundException;

public class DatabaseRecordNotFoundException extends NotFoundException {
  public DatabaseRecordNotFoundException(String message) {
    super(message);
  }
}
