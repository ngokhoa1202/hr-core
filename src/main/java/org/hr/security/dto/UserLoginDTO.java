package org.hr.security.dto;

import java.io.Serializable;

public record UserLoginDTO(String username, String password) implements Serializable {
}
