package org.hr.employee.service.grpc;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import lombok.RequiredArgsConstructor;
import org.gateway.service.project.assignment.AssignmentGrpcService;
import org.gateway.service.project.assignment.AssignmentPayloadProto;
import org.gateway.service.project.assignment.AssignmentResponseProto;
import org.hr.employee.dto.project.assignment.AssignmentMapper;
import org.hr.employee.dto.project.assignment.AssignmentPayloadDto;
import org.hr.employee.service.AssignmentService;
import org.hr.employee.service.messaging.MailingMessagingService;
import org.hr.exception.converter.ExceptionConverter;


@GrpcService
@RequiredArgsConstructor
public class AssignmentGrpcServiceImpl implements AssignmentGrpcService {

  private final AssignmentService assignmentService;
  private final ExceptionConverter exceptionConverter;
  private final MailingMessagingService mailingMessagingService;

  @Override
  public Uni<AssignmentResponseProto> createAssignment(AssignmentPayloadProto assignmentPayloadProto) {
    return Uni.createFrom().item(() -> {
      AssignmentPayloadDto assignmentPayloadDto = AssignmentMapper.INSTANCE.assignmentPayloadProtoToAssignmentPayloadDto(assignmentPayloadProto);
      return this.assignmentService.createAssignment(assignmentPayloadDto);
    })
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
      .onItem().transform((dto) -> {
        this.mailingMessagingService.send(dto);
        return AssignmentMapper.INSTANCE.assignmentResponseDtoToAssignmentResponseProto(dto);
      })
      .onFailure().transform((throwable) -> this.exceptionConverter.convert((RuntimeException) throwable));
  }

}
