package org.sotil.kuarkus.demo.infrastructure.dto;

public record ProductData(
        Long id,
        String code,
        String name,
        String description
) {

}
