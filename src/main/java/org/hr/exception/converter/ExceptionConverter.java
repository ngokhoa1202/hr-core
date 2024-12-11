package org.hr.exception.converter;

import org.hr.exception.HumanResourceException;

public interface ExceptionConverter {

  HumanResourceException convert(RuntimeException ex);
}
