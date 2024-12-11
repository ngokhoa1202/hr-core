package org.hr.employee.dto.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jboss.resteasy.reactive.DateFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record DepartmentPayloadDto(
  @NotBlank String name,
  @NotNull LocalDateTime startDate
) implements Serializable {

}
