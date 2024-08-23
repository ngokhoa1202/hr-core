package org.hr.security.dto;

import lombok.Builder;

import java.io.Serializable;

public record UserLoginDTO(String username, String password) implements Serializable {
}
