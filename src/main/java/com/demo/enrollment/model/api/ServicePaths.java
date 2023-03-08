package com.demo.enrollment.model.api;

public class ServicePaths {

    public static final String COURSES = "/courses";
    public static final String COURSE_BY_ID = COURSES + "/{id}";
    public static final String COURSE_ENROLLMENTS = COURSES + "/{id}/enrollments";

    public static final String STUDENTS = "/students";
    public static final String STUDENT_BY_ID = STUDENTS + "/{id}";
    public static final String STUDENT_ENROLLMENTS = STUDENTS + "/{id}/enrollments";
    public static final String GRADES = COURSES + "/{id}/grades";
}
