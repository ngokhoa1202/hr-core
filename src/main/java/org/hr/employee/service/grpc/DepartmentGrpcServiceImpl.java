package org.hr.employee.service.grpc;

import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import lombok.RequiredArgsConstructor;
import org.gateway.service.department.DepartmentGrpcService;
import org.gateway.service.department.DepartmentIdProto;
import org.gateway.service.department.DepartmentResponseProto;
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
    return Uni.createFrom().item(() -> this.departmentService.getDepartment(departmentIdProto.getId()))
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
      .onItem()
      .transform(DepartmentMapper.INSTANCE::departmentResponseDtoToDepartmentResponseProto)
      .onFailure()
      .transform((throwable) -> this.exceptionConverter.convert((RuntimeException) throwable));
  }


}
