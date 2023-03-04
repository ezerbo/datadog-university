package com.demo.enrollment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enrollment")
public class Enrollment {
	
	@EmbeddedId
	private EnrollmentId id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "enrollment_date", nullable = false)
	private Date enrollmentDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "student_id", nullable = false, insertable = false, updatable = false)
	private Student student;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "course_id", nullable = false, insertable = false, updatable = false)
	private Course course;
	
	@PrePersist
	public void onSave() {
		setEnrollmentDate(new Date());
	}
	
}