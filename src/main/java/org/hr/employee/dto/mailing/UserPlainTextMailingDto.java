package org.hr.employee.dto.mailing;

import java.util.UUID;

public record UserPlainTextMailingDto(
  UUID id,
  EmailPlainTextContentDto emailPlainTextContentDto
) {
}
