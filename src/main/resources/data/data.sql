-- students
insert into student(id, first_name, last_name, email_address, dob, ssn) values (1, 'Luffy'  , 'MONKEY D.', 'luffy.monkey@onpiece.com'      , dateadd('YEAR', -20, current_date()), '123456789');
insert into student(id, first_name, last_name, email_address, dob, ssn) values (2, 'Zoro'   , 'RORONOA'  , 'zoro.roronoa@onpiece.com'      , dateadd('YEAR', -20, current_date()), '123456788');
insert into student(id, first_name, last_name, email_address, dob, ssn) values (3, 'Sandji' , 'VINSMOKE' , 'sandji.vinsmoke@onpiece.com'   , dateadd('YEAR', -20, current_date()), '123456787');
insert into student(id, first_name, last_name, email_address, dob, ssn) values (4, 'Robin'  , 'NICO'     , 'robin.nico@onpiece.com'        , dateadd('YEAR', -20, current_date()), '123456786');
insert into student(id, first_name, last_name, email_address, dob, ssn) values (5, 'Chopper', 'TONY TONY', 'chopper.tonytony@onepiece.com' , dateadd('YEAR', -20, current_date()), '123456785');

-- courses
insert into course(id, name, start_date, end_date) values (1, 'Conqueror''s Haki'  , current_date(), dateadd('WEEK', 10, current_date()));

-- enrollments
insert into enrollment (student_id, course_id, enrollment_date) values (1, 1, current_date());
insert into enrollment (student_id, course_id, enrollment_date) values (2, 1, current_date());
insert into enrollment (student_id, course_id, enrollment_date) values (3, 1, current_date());
insert into enrollment (student_id, course_id, enrollment_date) values (4, 1, current_date());
insert into enrollment (student_id, course_id, enrollment_date) values (5, 1, current_date());