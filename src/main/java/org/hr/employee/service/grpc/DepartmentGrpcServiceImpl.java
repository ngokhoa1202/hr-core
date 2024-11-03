package org.hr.employee.service.grpc;

import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import lombok.RequiredArgsConstructor;
import org.gateway.service.department.*;
import org.hr.employee.dto.department.DepartmentMapper;
import org.hr.employee.service.DepartmentService;
import org.hr.exception.converter.ExceptionConverter;

@GrpcService
@RequiredArgsConstructor
public class DepartmentGrpcServiceImpl implements DepartmentGrpcService {

  private final DepartmentService departmentService;

  private final ExceptionConverter exceptionConverter;

  @Override
  public Uni<DepartmentResponseProto> getDepartmentById(DepartmentIdProto departmentIdProto) throws StatusRuntimeException {
    return Uni.createFrom().item(departmentIdProto)
      .onItem().transform(DepartmentIdProto::getId)
      .onItem().transform(this.departmentService::getDepartment)
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
      .onItem().transform(DepartmentMapper.INSTANCE::departmentResponseDtoToDepartmentResponseProto)
      .onFailure().transform((throwable) -> this.exceptionConverter.convert((RuntimeException) throwable));
  }

  @Override
  public Uni<DepartmentResponseProto> createDepartment(DepartmentPayloadProto departmentPayloadProto) throws StatusRuntimeException {
    return Uni.createFrom().item(departmentPayloadProto)
      .onItem().transform(DepartmentMapper.INSTANCE::departmentPayloadProtoToDepartmentPayloadDto)
      .onItem().transform(this.departmentService::createDepartment)
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
      .onItem().transform(DepartmentMapper.INSTANCE::departmentResponseDtoToDepartmentResponseProto)
      .onFailure().transform((throwable) -> this.exceptionConverter.convert((RuntimeException) throwable));
  }

  @Override
  public Uni<DepartmentResponseProto> updateDepartment(DepartmentPlainProto departmentPlainProto) {

    return Uni.createFrom().item(departmentPlainProto)
      .onItem().transform((proto) ->
        this.departmentService.updateDepartment(
          proto.getId(),
          DepartmentMapper.INSTANCE.departmentPlainProtoToDepartmentPayloadDto(proto)
        )
      )
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
      .onItem().transform(DepartmentMapper.INSTANCE::departmentResponseDtoToDepartmentResponseProto)
      .onFailure().transform((throwable) -> this.exceptionConverter.convert((RuntimeException) throwable));
  }

  @Override
  public Uni<Empty> deleteDepartment(DepartmentIdProto departmentIdProto) {
    return Uni.createFrom().item(departmentIdProto)
      .onItem().invoke((proto) -> {
        this.departmentService.deleteDepartment(proto.getId());
      })
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
      .replaceWith(Empty.getDefaultInstance())
      .onFailure()
      .transform((throwable) -> this.exceptionConverter.convert((RuntimeException) throwable));
  }
}
