package org.hr.employee.service.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.grpc.common.GrpcStatus;
import lombok.RequiredArgsConstructor;
import org.gateway.exception.ExceptionProto;
import org.gateway.service.department.DepartmentGrpcService;
import org.gateway.service.department.DepartmentGrpcServiceGrpc;
import org.gateway.service.department.DepartmentIdProto;
import org.gateway.service.department.DepartmentResponseProto;
import org.hr.employee.dto.department.DepartmentMapper;
import org.hr.employee.service.DepartmentService;
import org.hr.exception.handler.ExceptionConverter;
import org.hr.exception.mapper.ErrorResponseBody;
import org.hr.exception.mapper.HumanResourceException;

@GrpcService
@RequiredArgsConstructor
public class DepartmentGrpcServiceImpl implements DepartmentGrpcService {

  private final DepartmentService departmentService;

  private final ExceptionConverter exceptionConverter;

  @Override
  public Uni<DepartmentResponseProto> getDepartmentById(DepartmentIdProto departmentIdProto) {
    return Uni.createFrom().item(() -> this.departmentService.getDepartment(departmentIdProto.getId()))
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
      .onItem()
      .transform(DepartmentMapper.INSTANCE::departmentResponseDtoToDepartmentResponseProto)
      .onFailure()
      .transform((throwable) -> {
        HumanResourceException ex = this.exceptionConverter.convert((RuntimeException) throwable);
        ErrorResponseBody body = ex.getResponse().readEntity(ErrorResponseBody.class);
        ExceptionProto exceptionProto = ExceptionProto.newBuilder()
          .setStatus(body.status())
          .setMessage(body.message())
          .setField(body.field())
          .setTimeStamp(body.timeStamp().toString())
          .build();
        com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
          .setCode(Status.INVALID_ARGUMENT.getCode().value())
          .setMessage(body.message())
          .build();
        return new StatusRuntimeException(Status.INVALID_ARGUMENT);
      });
  }


}
