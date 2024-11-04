package org.hr.employee.service.messaging;

import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.hr.employee.dto.mailing.EmailPlainTextContentDto;
import org.hr.employee.dto.mailing.UserPlainTextMailingDto;
import org.hr.employee.dto.project.assignment.AssignmentResponseDto;

import java.time.ZonedDateTime;

@ApplicationScoped
public class MailingMessagingService {

  @Channel("userId")
  protected Emitter<UserPlainTextMailingDto> userEmailEmitter;

  public void send(AssignmentResponseDto assignmentResponseDto) {
    final UserPlainTextMailingDto userPlainTextMailingDto = this.createUserPlainTextMailingDto(assignmentResponseDto);
    final OutgoingRabbitMQMetadata metadata = new OutgoingRabbitMQMetadata.Builder()
      .withHeader("content_type", "application/json")
      .withRoutingKey("user*")
      .withTimestamp(ZonedDateTime.now())
      .build();
    final Message<UserPlainTextMailingDto> message = Message.of(userPlainTextMailingDto, Metadata.of(metadata));
    this.userEmailEmitter.send(message);
  }

  private UserPlainTextMailingDto createUserPlainTextMailingDto(AssignmentResponseDto assignmentResponseDto) {
    return new UserPlainTextMailingDto(
      assignmentResponseDto.employeePlainDto().id(),
      new EmailPlainTextContentDto(
        String.format(
          "Assignment %s assigned",
          assignmentResponseDto.id()
        ),
        String.format(
          "Assignment %s on project %s has been assigned to you",
          assignmentResponseDto.id(),
          assignmentResponseDto.projectPlainDto().name()
        )
      )
    );
  }
}
