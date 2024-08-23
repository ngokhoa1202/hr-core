package org.hr.security.resource;

import io.quarkus.smallrye.jwt.runtime.auth.BearerTokenAuthentication;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.MockitoConfig;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.hr.employee.dto.DepartmentCreationDTO;
import org.hr.security.dto.JwtDTO;
import org.hr.security.dto.UserLoginDTO;
import org.hr.security.service.AuthenticationService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.sql.Date;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(AuthenticationResource.class)
public class AuthenticationResourceTest {

  private UserLoginDTO mockUserLoginDTOAsAdmin() {
    return new UserLoginDTO("ngovuanhkhoa", "1234");
  }

  @Test
  @Order(1)
  public void shouldReturnJwtTokenWhenLoggingIn() {
    final UserLoginDTO userLoginDTO = this.mockUserLoginDTOAsAdmin();

    JwtDTO jwtDTO = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .body(userLoginDTO)
      .when().post("login")
      .then()
      .statusCode(Response.Status.OK.getStatusCode())
      .extract().body().as(JwtDTO.class);
    assertNotNull(jwtDTO);
  }

}
