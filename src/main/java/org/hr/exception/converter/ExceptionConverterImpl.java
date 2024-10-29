package org.hr.exception.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hr.employee.entity.Project;
import org.hr.exception.*;

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Optional;

import static org.hr.employee.utils.ConstraintMessage.*;
import static org.postgresql.util.PSQLState.*;

@ApplicationScoped
public class ExceptionConverterImpl implements ExceptionConverter {

  protected Optional<String> getFieldNameFrom(String constraintName) {
    if (constraintName == null) {
      return null;
    }
    int lengthOfFieldName = constraintName.lastIndexOf("_");
    String fieldName = constraintName.substring(0, lengthOfFieldName);
    return Optional.of(fieldName);
  }

  protected HumanResourceException convertHibernateException(
    org.hibernate.exception.ConstraintViolationException ex) {

    String sqlState = ex.getSQLState().toLowerCase(Locale.ROOT);
    String columnName = this.getFieldNameFrom(ex.getConstraintName()).get();
    if (sqlState.equals(UNIQUE_VIOLATION.getState())) {
      return new DuplicateFieldException(columnName);
    }
    return new InvalidRequestBodyException(ex.getMessage());
  }

  protected String getMessageFromConstraintViolation(ConstraintViolation<?> violation) {
    String property = violation.getPropertyPath().toString();
    Annotation annotation = violation.getConstraintDescriptor().getAnnotation();
    switch (annotation) {
      case NotNull constraint -> {
        return String.format("The %s must %s", property, NOT_NULL_CONSTRAINT);
      }
      case NotBlank constraint -> {
        return String.format("The %s must %s", property, NOT_BLANK_CONSTRAINT);
      }
      case PositiveOrZero constraint -> {
        return String.format("The %s must %s", property, POSITIVE_OR_ZERO_CONSTRAINT);
      }
      case Pattern constraint -> {
        if (property.contains("name") ||
          (property.equals("area") && violation.getRootBean() instanceof Project)
        ) {
          return String.format("The %s must %s", property, NAME_REGEX_CONSTRAINT);
        }
        return String.format("The %s must %s", property, constraint.message());
      }
      default -> {
        return null;
      }
    }
  }

  protected HumanResourceException convertJakartaValidationException(ConstraintViolationException ex) {
    ConstraintViolation<?> violation = ex.getConstraintViolations()
      .stream().findFirst()
      .get();
    String property = violation.getPropertyPath().toString();

    return new InvalidFieldException(
      property,
      this.getMessageFromConstraintViolation(violation)
    );
  }


  @Override
  public HumanResourceException convert(RuntimeException ex) {
    switch (ex) {
      case org.hibernate.exception.ConstraintViolationException exc -> {
        return this.convertHibernateException(exc);
      }

      case jakarta.validation.ConstraintViolationException exc -> {
        return this.convertJakartaValidationException(exc);
      }

      case EntityNotFoundException exc -> {
        return exc;
      }

      case UnauthorizedException exc -> {
        return exc;
      }

      case DuplicateFieldException exc -> {
        return exc;
      }

      default -> {
        return new InvalidRequestBodyException(ex.getMessage());
      }
    }
  }
}
