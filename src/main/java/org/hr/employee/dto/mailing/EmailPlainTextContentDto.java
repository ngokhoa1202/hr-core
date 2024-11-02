package org.hr.employee.dto.mailing;


import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public record EmailPlainTextContentDto(
  String subject,
  String body
) implements Serializable {

}
