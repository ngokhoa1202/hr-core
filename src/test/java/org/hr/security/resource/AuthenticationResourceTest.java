package org.hr.security.resource;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpHeaders;
import org.hr.security.dto.JwtDto;
import org.hr.security.dto.user.UserLoginDto;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(AuthenticationResource.class)
public class AuthenticationResourceTest {

  private UserLoginDto mockUserLoginDTOAsAdmin() {
    return new UserLoginDto("ngovuanhkhoa", "1234");
  }

  @Test
  @Order(1)
  public void shouldReturnJwtTokenWhenLoggingIn() {
    final UserLoginDto userLoginDTO = this.mockUserLoginDTOAsAdmin();

    JwtDto jwtDTO = given()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .body(userLoginDTO)
      .when().post("login")
      .then()
      .statusCode(Response.Status.OK.getStatusCode())
      .extract().body().as(JwtDto.class);
    assertNotNull(jwtDTO);
  }

}
