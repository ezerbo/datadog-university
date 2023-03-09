-- students
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (1, 'Luffy'  , 'MONKEY D.', 'luffy.monkey@onpiece.com'      , dateadd('YEAR', -20, current_date()), '123456789', 1);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (2, 'Zoro'   , 'RORONOA'  , 'zoro.roronoa@onpiece.com'      , dateadd('YEAR', -20, current_date()), '123456788', 2);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (3, 'Sandji' , 'VINSMOKE' , 'sandji.vinsmoke@onpiece.com'   , dateadd('YEAR', -20, current_date()), '123456787', 3);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (4, 'Robin'  , 'NICO'     , 'robin.nico@onpiece.com'        , dateadd('YEAR', -20, current_date()), '123456786', 4);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (5, 'Chopper', 'TONY TONY', 'chopper.tonytony@onepiece.com' , dateadd('YEAR', -20, current_date()), '123456785', 5);

-- courses
insert into course(id, name, start_date, end_date) values (1, 'Conqueror''s Haki', current_date(), dateadd('WEEK', 10, current_date()));

-- enrollments
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (1, 1, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (2, 2, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (3, 3, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (4, 4, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (5, 5, 1, current_date());