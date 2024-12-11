package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hr.employee.utils.CommonRegex;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

  @Id
  @Column(name = "project_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NotBlank
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  private String area;

  @Column(name = "project_name")
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  private String name;

  private String description;

  @ManyToOne(
    fetch = FetchType.EAGER,
    cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
  )
  @JoinColumn(
    name = "department_id",
    referencedColumnName = "department_id"
  )
  private Department departmentBelonging;

  @OneToMany(
    mappedBy = "projectBelonging",
    fetch = FetchType.LAZY,
     cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  @Builder.Default
  private Set<Assignment> assignments = new HashSet<Assignment>();

}
