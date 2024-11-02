package org.hr.employee.dto.mailing;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.UUID;

@RegisterForReflection
public record UserPlainTextMailingDto(
  UUID id,
  EmailPlainTextContentDto emailPlainTextContentDto
) {
}
