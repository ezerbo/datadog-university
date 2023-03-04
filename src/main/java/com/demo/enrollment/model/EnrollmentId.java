package com.demo.enrollment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentId implements Serializable {
	
	private static final long serialVersionUID = -2124715621667674123L;
	
	@Column(name = "student_id", nullable = false)
	private Long studentId;
	
	@Column(name = "course_id", nullable = false)
	private Long courseId;
	
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (!(other instanceof EnrollmentId)) return false;
		EnrollmentId castOther = (EnrollmentId)other;
		return (Objects.equals(this.studentId, castOther.studentId))
				&& (Objects.equals(this.courseId, castOther.courseId));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.studentId.intValue();
		hash = hash * prime + this.courseId.intValue();
		return hash;
	}
}