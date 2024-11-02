package org.hr.employee.dto.mailing;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.FormParam;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

@RegisterForReflection
public record EmailWithAttachmentsContentDto(
  String subject,
  String body,
  @FormParam(value = "attachments") List<FileUpload> attachments
) {
}
