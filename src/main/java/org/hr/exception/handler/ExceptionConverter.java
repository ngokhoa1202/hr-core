package org.hr.exception.handler;

import org.hr.exception.mapper.HumanResourceException;

public interface ExceptionConverter {

  HumanResourceException convert(RuntimeException ex);
}
