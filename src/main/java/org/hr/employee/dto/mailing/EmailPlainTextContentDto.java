package org.hr.employee.dto.mailing;

public record EmailPlainTextContentDto(
  String subject,
  String body
) {
}
