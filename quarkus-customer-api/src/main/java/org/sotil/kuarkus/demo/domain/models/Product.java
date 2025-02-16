package org.sotil.kuarkus.demo.domain.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(
  uniqueConstraints =
  @UniqueConstraint(columnNames = {"customer", "product"})
)
public class Product extends PanacheEntity {

  @ManyToOne
  @JoinColumn(name = "customer", referencedColumnName = "id")
  @JsonBackReference
  private Customer customer;
  @Column
  private Long product;

  @Transient
  private Long id;
  @Transient
  private String name;
  @Transient
  private String code;
  @Transient
  private String description;
}
