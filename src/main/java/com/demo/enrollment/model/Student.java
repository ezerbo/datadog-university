package com.demo.enrollment.model;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
@ToString(exclude = {"assignments"})
@EqualsAndHashCode(exclude = {"assignments"})
public class Student {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email
    @NotBlank
    @Column(name = "email_address", unique = true, nullable = false)
    private String emailAddress;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dob", nullable = false)
    private Date dob;

    @NotBlank
    @Column(name = "ssn", nullable = false)
    private String ssn;

    @JsonIgnore
    @OneToMany(mappedBy = "student", orphanRemoval = true)
    private List<Enrollment> enrollments;
}
