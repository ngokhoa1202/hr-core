package org.hr.employee.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Method;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.protocol.HTTP;
import org.hr.employee.dto.DepartmentCreationDTO;
import org.hr.employee.dto.DepartmentDTO;
import org.hr.employee.dto.DepartmentLocationCreationDTO;
import org.hr.employee.dto.DepartmentLocationDTO;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;
import org.hr.employee.scenario.DepartmentTestScenario;
import org.hr.employee.service.DepartmentService;
import org.hr.employee.utils.ConstraintMessage;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.mapper.ErrorResponseBody;
import org.hr.security.dto.JwtDTO;
import org.hr.security.dto.UserLoginDTO;
import org.hr.security.resource.AuthenticationResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(value = DepartmentResource.class)
public class DepartmentResourceTest {

  @TestHTTPEndpoint(value = AuthenticationResource.class)
  @TestHTTPResource(value = "login")
  private URL loginEndpoint;

  private String jwt;

  @InjectMock
  private DepartmentService departmentService;

  @Inject
  protected DepartmentTestScenario scenario;


  @BeforeEach
  public void setUp() {
    this.jwt = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .body(
        new UserLoginDTO("ngovuanhkhoa", "1234")
      )
      .when().request(Method.POST, this.loginEndpoint)
      .then()
      .statusCode(Response.Status.OK.getStatusCode())
      .extract().body().as(JwtDTO.class).token();
  }


  private void assertErrorResponseBodyForEntityNotFound(ErrorResponseBody body, String entityName) {
    assertEquals(body.status(), Response.Status.NOT_FOUND.getStatusCode());
    assertEquals(body.message(), String.format("The %s is not found", entityName));
    assertInstanceOf(LocalDateTime.class, body.timeStamp());
  }

  private void assertErrorResponseBodyForFieldExisted(ErrorResponseBody body, String fieldName) {
    assertEquals(body.status(), Response.Status.CONFLICT.getStatusCode());
    assertEquals(body.message(), String.format("The %s field has already existed", fieldName));
    assertInstanceOf(LocalDateTime.class, body.timeStamp());
  }

  private void assertErrorResponseBodyForInvalidField(ErrorResponseBody body, String fieldName, String expectedMessage) {
    assertEquals(body.status(), Response.Status.BAD_REQUEST.getStatusCode());
    assertEquals(body.message(), expectedMessage);
    assertInstanceOf(LocalDateTime.class, body.timeStamp());
  }


  @Test
  @Order(1)
  public void GivenValidDepartmentId_WhenGettingDepartmentById_ReturnDepartmentDTO() {

    final DepartmentDTO expectedDepartmentDTO = this.scenario.mockDepartmentDTO();

    Mockito.when(this.departmentService.getDepartment(expectedDepartmentDTO.id()))
      .thenReturn(expectedDepartmentDTO);

    DepartmentDTO departmentDTO = given()
      .header(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .pathParam("id", expectedDepartmentDTO.id())
      .when().get("{id}")
      .then()
      .statusCode(Response.Status.OK.getStatusCode())
      .extract().body()
      .as(DepartmentDTO.class);

    assertEquals(expectedDepartmentDTO, departmentDTO);
  }

  @Test
  @Order(2)
  public void GivenDepartmentIdNotExisted_WhenGettingDepartmentById_ReturnRequestNotFound() {
    final Long id = this.scenario.mockDepartmentId();

    Mockito.when(this.departmentService.getDepartment(id))
      .thenThrow(new EntityNotFoundException(Department.class.getName()));

    ErrorResponseBody body = given()
      .header(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .pathParam("id", id)
      .when().get("{id}")
      .then()
      .statusCode(Response.Status.NOT_FOUND.getStatusCode())
      .extract()
      .body().as(ErrorResponseBody.class);
    this.assertErrorResponseBodyForEntityNotFound(body, Department.class.getName());
  }

  @Test
  @Order(3)
  public void GivenDepartmentNameAlreadyExisted_WhenCreatingDepartment_ReturnRequestConflict() {

    final DepartmentCreationDTO departmentCreationDTO = this.scenario.mockDepartmentCreationDTO();

    Mockito.when(this.departmentService.createDepartment(departmentCreationDTO)).thenThrow(
      this.scenario.mockHibernateUniqueViolationException("department_name_unique")
    );

    ErrorResponseBody responseBody = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .body(departmentCreationDTO)
      .when().post()
      .then()
      .statusCode(Response.Status.CONFLICT.getStatusCode())
      .extract().body().as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForFieldExisted(responseBody, "department_name");
  }

  @Test
  @Order(4)
  public void GivenRequestLackingJwtToken_WhenCreatingDepartment_ReturnRequestUnauthorized() {
    final DepartmentCreationDTO departmentCreationDTO = this.scenario.mockDepartmentCreationDTO();

    given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .body(departmentCreationDTO)
      .when().post()
      .then()
      .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
  }

  @Test
  @Order(5)
  public void GivenValidDepartmentCreationDTO_WhenCreatingDepartment_ReturnDepartmentCreated() {
    final DepartmentCreationDTO departmentCreationDTO = this.scenario.mockDepartmentCreationDTO();
    final DepartmentDTO departmentDTO = this.scenario.mockDepartmentDTO();

    Mockito.when(this.departmentService.createDepartment(departmentCreationDTO))
      .thenReturn(departmentDTO);

    URI departmentURI = URI.create(
      given()
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
        .auth().oauth2(this.jwt)
        .body(departmentCreationDTO)
        .when().post()
        .then()
        .statusCode(Response.Status.CREATED.getStatusCode())
        .extract()
        .header(HttpHeaders.LOCATION)
    );
    assertNotNull(departmentURI);
  }



  @Test
  @Order(6)
  public void GivenInvalidDepartmentName_WhenCreatingDepartment_ReturnBadRequest() {
    final DepartmentCreationDTO departmentCreationDTO = this.scenario.mockDepartmentCreationDTO();

    Mockito.when(this.departmentService.createDepartment(departmentCreationDTO)).thenThrow(
      this.scenario.mockJakartaConstraintViolationException(
        this.scenario.mockDepartmentCreationDTOWithInvalidName().toDepartment()
      )
    );

    ErrorResponseBody responseBody = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .body(departmentCreationDTO)
      .when().post()
      .then()
      .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
      .extract()
      .body().as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForInvalidField(
      responseBody,
      "name",
      String.format("The %s must %s", "name", ConstraintMessage.NAME_REGEX_CONSTRAINT)
    );
  }

  @Test
  @Order(7)
  public void GivenValidDepartmentDTO_WhenUpdatingDepartment_ReturnDepartmentCreated() {
    Long departmentId = this.scenario.mockDepartmentId();
    final DepartmentCreationDTO departmentCreationDTO = this.scenario.mockDepartmentCreationDTO();
    final DepartmentDTO expectedDepartmentDTO = this.scenario.mockDepartmentDTO();

    Mockito.when(this.departmentService.updateDepartment(departmentId, departmentCreationDTO))
      .thenReturn(expectedDepartmentDTO);

    URI departmentURI = URI.create(
      given()
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
        .auth().oauth2(this.jwt)
        .pathParam("id", departmentId)
        .body(departmentCreationDTO)
        .when().put("{id}")
        .then()
        .statusCode(Response.Status.CREATED.getStatusCode())
        .extract()
        .header(HttpHeaders.LOCATION)
    );
    assertNotNull(departmentURI);
  }

  @Test
  @Order(7)
  public void GivenNotFoundDepartmentId_WhenUpdatingDepartment_ReturnRequestNotFound() {
    final Long departmentId = this.scenario.mockDepartmentId();
    final DepartmentCreationDTO departmentCreationDTO = this.scenario.mockDepartmentCreationDTO();

    Mockito.when(this.departmentService.updateDepartment(departmentId, departmentCreationDTO))
      .thenThrow(new EntityNotFoundException(Department.class.getName()));

    ErrorResponseBody responseBody = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .pathParam("id", departmentId)
      .body(departmentCreationDTO)
      .when().put("{id}")
      .then()
      .statusCode(Response.Status.NOT_FOUND.getStatusCode())
      .extract().as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForEntityNotFound(responseBody, Department.class.getName());
  }

  @Test
  @Order(8)
  public void GivenDepartmentNameExisted_WhenUpdatingDepartment_ReturnRequestConflict() {
    final Long departmentId = this.scenario.mockDepartmentId();
    final DepartmentCreationDTO departmentCreationDTO = this.scenario.mockDepartmentCreationDTO();

    Mockito.when(this.departmentService.updateDepartment(departmentId, departmentCreationDTO))
      .thenThrow(this.scenario.mockHibernateUniqueViolationException("department_name_unique"));

    ErrorResponseBody body = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .pathParam("id", departmentId)
      .body(departmentCreationDTO)
      .when().put("{id}")
      .then()
      .statusCode(Response.Status.CONFLICT.getStatusCode())
      .extract().as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForFieldExisted(body, "department_name");
  }

  @Test
  @Order(7)
  public void GivenDepartmentId_WhenDeletingDepartment_ReturnNoContent() {
    final Long departmentId = this.scenario.mockDepartmentId();

    Mockito.doNothing().when(this.departmentService).deleteDepartmentLocation(departmentId);
    given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .pathParam("id", departmentId)
      .when().delete("{id}")
      .then()
      .statusCode(Response.Status.NO_CONTENT.getStatusCode());
  }

  @Test
  @Order(8)
  public void GivenDepartmentLocationId_WhenGettingDepartmentLocationById_ReturnDepartmentLocationDTO() {
    final Long departmentLocationId = this.scenario.mockDepartmentLocationId();
    final DepartmentLocationDTO expectedDepartmentLocationDTO = this.scenario.mockDepartmentLocationDTO();

    Mockito.when(this.departmentService.getDepartmentLocation(departmentLocationId))
      .thenReturn(expectedDepartmentLocationDTO);

    DepartmentLocationDTO departmentLocationDTO = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .pathParam("id", departmentLocationId)
      .when().get("locations/{id}")
      .then()
      .statusCode(Response.Status.OK.getStatusCode())
      .extract().body()
      .as(DepartmentLocationDTO.class);

    assertEquals(departmentLocationDTO, expectedDepartmentLocationDTO);
  }

  @Test
  @Order(9)
  public void GivenDepartmentLocationIdNotExisted_WhenGettingDepartmentLocationById_ReturnRequestNotFound() {
    final Long departmentLocationId = this.scenario.mockDepartmentLocationId();

    Mockito.when(this.departmentService.getDepartmentLocation(departmentLocationId))
      .thenThrow(new EntityNotFoundException(DepartmentLocation.class.getName()));

    ErrorResponseBody responseBody = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .pathParam("id", departmentLocationId)
      .when().get("locations/{id}")
      .then()
      .statusCode(Response.Status.NOT_FOUND.getStatusCode())
      .extract().body()
      .as(ErrorResponseBody.class);
    this.assertErrorResponseBodyForEntityNotFound(responseBody, DepartmentLocation.class.getName());
  }

  @Test
  @Order(10)
  public void GivenValidDepartmentLocationDTO_WhenCreatingDepartmentLocation_ReturnDepartmentLocationCreated() {
    final DepartmentLocationDTO expectedLocationDTO = this.scenario.mockDepartmentLocationDTO();
    final DepartmentLocationCreationDTO departmentLocationCreationDTO = this.scenario.mockDepartmentLocationCreationDTO();

    Mockito.when(this.departmentService.createDepartmentLocation(departmentLocationCreationDTO))
      .thenReturn(expectedLocationDTO);

    URI locationURI = URI.create(
      given()
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .auth().oauth2(this.jwt)
        .body(departmentLocationCreationDTO)
        .when().post("locations")
        .then()
        .statusCode(Response.Status.CREATED.getStatusCode())
        .extract()
        .header(HttpHeaders.LOCATION)
    );

    assertNotNull(locationURI);
  }

  @Test
  @Order(11)
  public void GivenDepartmentIdNotExisted_WhenCreatingDepartmentLocation_ReturnRequestNotFound() {
    final DepartmentLocationCreationDTO locationCreationDTO = this.scenario.mockDepartmentLocationCreationDTO();

    Mockito.when(this.departmentService.createDepartmentLocation(locationCreationDTO))
      .thenThrow(new EntityNotFoundException(DepartmentLocation.class.getName()));

    ErrorResponseBody body = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .body(locationCreationDTO)
      .when().post("locations")
      .then()
      .statusCode(Response.Status.NOT_FOUND.getStatusCode())
      .extract().body()
      .as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForEntityNotFound(body, DepartmentLocation.class.getName());
  }


  @Test
  @Order(12)
  public void GivenInvalidJwtToken_WhenCreatingDepartmentLocation_ReturnRequestUnauthorized() {
    final DepartmentLocationCreationDTO locationCreationDTO = this.scenario.mockDepartmentLocationCreationDTO();

    Mockito.when(this.departmentService.createDepartmentLocation(locationCreationDTO))
      .thenThrow(new EntityNotFoundException(DepartmentLocation.class.getName()));

    given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.scenario.mockInvalidJwtToken())
      .body(locationCreationDTO)
      .when().post("locations")
      .then()
      .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
  }

  @Test
  @Order(13)
  public void GivenDepartmentLocationAlreadyExisted_WhenCreatingDepartmentLocation_ReturnRequestConflict() {
    final DepartmentLocationCreationDTO locationCreationDTO = this.scenario.mockDepartmentLocationCreationDTO();

    Mockito.when(this.departmentService.createDepartmentLocation(locationCreationDTO))
      .thenThrow(this.scenario.mockHibernateUniqueViolationException("department_location_unique"));

    ErrorResponseBody body =  given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .body(locationCreationDTO)
      .when().post("locations")
      .then()
      .statusCode(Response.Status.CONFLICT.getStatusCode())
      .extract().body()
      .as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForFieldExisted(body, "department_location");
  }

  @Test
  @Order(14)
  public void GivenInvalidDepartmentLocation_WhenCreatingDepartmentLocation_ReturnBadRequest() {
    final DepartmentLocationCreationDTO locationCreationDTO = this.scenario.mockDepartmentLocationCreationDTO();

    Mockito.when(this.departmentService.createDepartmentLocation(locationCreationDTO))
      .thenThrow(
        this.scenario.mockJakartaConstraintViolationException(
          this.scenario.mockDepartmentLocationCreationDTOWithInvalidLocation().toDepartmentLocation()
        )
      );

    ErrorResponseBody body = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .body(locationCreationDTO)
      .when().post("locations")
      .then()
      .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
      .extract().body()
      .as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForInvalidField(
      body,
      "department_location",
      String.format("The %s must %s", "location", ConstraintMessage.NOT_BLANK_CONSTRAINT)
    );
  }

  @Test
  @Order(15)
  public void GivenInvalidJwtToken_WhenUpdatingDepartmentLocation_ReturnRequestUnauthorized() {
    final DepartmentLocationCreationDTO locationCreationDTO = this.scenario.mockDepartmentLocationCreationDTO();

    given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.scenario.mockInvalidJwtToken())
      .body(locationCreationDTO)
      .when().post("locations")
      .then()
      .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
  }

  @Test
  @Order(16)
  public void GivenDepartmentIdNotExisted_WhenUpdatingDepartmentLocation_ReturnRequestNotFound() {
    final DepartmentLocationCreationDTO locationCreationDTO = this.scenario.mockDepartmentLocationCreationDTO();
    final Long id = this.scenario.mockDepartmentLocationId();

    Mockito.when(this.departmentService.updateDepartmentLocation(id, locationCreationDTO))
      .thenThrow(new EntityNotFoundException(Department.class.getName()));

    ErrorResponseBody body = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .pathParam("id", id)
      .body(locationCreationDTO)
      .when().put("locations/{id}")
      .then()
      .statusCode(Response.Status.NOT_FOUND.getStatusCode())
      .extract().body()
      .as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForEntityNotFound(body, Department.class.getName());
  }

  @Test
  @Order(19)
  public void GivenDepartmentLocationAlreadyExisted_WhenUpdatingDepartmentLocation_ReturnRequestConflict() {
    final Long id = this.scenario.mockDepartmentLocationId();
    final DepartmentLocationCreationDTO locationCreationDTO = this.scenario.mockDepartmentLocationCreationDTO();

    Mockito.when(this.departmentService.updateDepartmentLocation(id, locationCreationDTO))
      .thenThrow(this.scenario.mockHibernateUniqueViolationException("department_location_unique"));


    ErrorResponseBody body = given()
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
        .auth().oauth2(this.jwt)
        .pathParam("id", id)
        .body(locationCreationDTO)
        .when().put("locations/{id}")
        .then()
        .statusCode(Response.Status.CONFLICT.getStatusCode())
        .extract().body()
        .as(ErrorResponseBody.class);

    this.assertErrorResponseBodyForFieldExisted(body, "department_location");
  }

  @Test
  @Order(17)
  public void GivenInvalidJwtToken_WhenDeletingDepartmentLocation_ReturnRequestUnauthorized() {
    Long id = this.scenario.mockDepartmentLocationId();

    given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.scenario.mockInvalidJwtToken())
      .pathParam("id", id)
      .when().delete("locations/{id}")
      .then()
      .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
  }

  @Test
  @Order(18)
  public void GivenDepartmentLocationId_WhenDeletingDepartmentLocation_ReturnNoContent() {
    Long id = this.scenario.mockDepartmentLocationId();

    Mockito.doNothing().when(this.departmentService).deleteDepartmentLocation(id);

    given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .auth().oauth2(this.jwt)
      .pathParam("id", id)
      .when().delete("locations/{id}")
      .then()
      .statusCode(Response.Status.NO_CONTENT.getStatusCode());
  }


}