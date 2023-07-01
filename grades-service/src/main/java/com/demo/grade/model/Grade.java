package com.demo.grade.model;

import com.demo.grade.model.api.SupportedGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "course_id")
    private Long courseId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private SupportedGrade grade;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;

    @PrePersist
    public void onCreate() {
        setCreateDate(new Date());
        setGrade(SupportedGrade.NOT_YET_SUBMITTED);
        setUpdateDate(new Date());
    }

    @PreUpdate
    public void onUpdate() {
        setUpdateDate(new Date());
    }
}
