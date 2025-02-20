package org.sotil.kuarkus.demo.domain;


public record UserDTO(
        String username,
        String roles,
        String password) {

}
