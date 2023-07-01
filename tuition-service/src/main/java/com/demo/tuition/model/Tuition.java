package com.demo.tuition.model;

import static javax.persistence.GenerationType.IDENTITY;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tuition")
public class Tuition {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "student_id", nullable = false, unique = true)
    private Long studentId;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "paid", nullable = false)
    private boolean paid;
}
