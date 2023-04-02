-- students
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (1 , 'Luffy'  , 'Monkey D.'  , 'luffy.monkey@onpiece.com'      , dateadd('YEAR', -20, current_date()), '123456789', 1);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (2 , 'Zoro'   , 'Roronoa'    , 'zoro.roronoa@onpiece.com'      , dateadd('YEAR', -20, current_date()), '123456788', 2);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (3 , 'Sandji' , 'Vinsmoke'   , 'sandji.vinsmoke@onpiece.com'   , dateadd('YEAR', -20, current_date()), '123456787', 3);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (4 , 'Robin'  , 'Nico'       , 'robin.nico@onpiece.com'        , dateadd('YEAR', -20, current_date()), '123456786', 4);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (5 , 'Chopper', 'Tony Tony'  , 'chopper.tonytony@onepiece.com' , dateadd('YEAR', -20, current_date()), '123456785', 5);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (6 , 'Garp'   , 'Monkey D.'  , 'garp.monkey@onepiece.com'      , dateadd('YEAR', -20, current_date()), '123456785', 6);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (7 , 'Dragon' , 'Monkey D.'  , 'dragon.monkey@onepiece.com'    , dateadd('YEAR', -20, current_date()), '123456785', 7);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (8 , 'Ace'    , 'Portgas D.', 'ace.portgas@onepiece.com'       , dateadd('YEAR', -20, current_date()), '123456785', 8);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (9 , 'Usopp'  , 'Sogeking'   , 'usopp.sogeking@onepiece.com'   , dateadd('YEAR', -20, current_date()), '123456785', 9);
insert into student(id, first_name, last_name, email_address, dob, ssn, tuition_id) values (10, 'Jinbe' , 'Jinbe'      , 'jinbe.jinbe@onepiece.com'       , dateadd('YEAR', -20, current_date()), '123456785', 10);

-- courses
insert into course(id, name, start_date, end_date) values (1, 'Conqueror''s Haki', current_date(), dateadd('WEEK', 10, current_date()));
insert into course(id, name, start_date, end_date) values (2, 'Devil Fruit''s Awakening', current_date(), dateadd('WEEK', 10, current_date()));
insert into course(id, name, start_date, end_date) values (3, 'Using the Color of the Supreme King', current_date(), dateadd('WEEK', 10, current_date()));
insert into course(id, name, start_date, end_date) values (4, 'Fighting an Emperor of the Sea', current_date(), dateadd('WEEK', 10, current_date()));
insert into course(id, name, start_date, end_date) values (5, 'Finding the East Blue', current_date(), dateadd('WEEK', 10, current_date()));
insert into course(id, name, start_date, end_date) values (6, 'Finding One Piece', current_date(), dateadd('WEEK', 10, current_date()));

-- enrollments
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (1, 1, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (2, 1, 2, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (3, 1, 3, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (4, 1, 4, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (5, 1, 5, current_date());

insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (6,  2, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (7,  2, 2, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (8,  2, 3, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (9,  2, 4, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (10, 2, 5, current_date());

insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (11, 3, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (12, 3, 2, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (13, 3, 3, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (14, 3, 4, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (15, 3, 5, current_date());

insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (16, 4, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (17, 4, 2, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (18, 4, 3, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (19, 4, 4, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (20, 4, 5, current_date());

insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (21, 5, 1, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (22, 5, 2, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (23, 5, 3, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (24, 5, 4, current_date());
insert into enrollment (grade_id, student_id, course_id, enrollment_date) values (25, 5, 5, current_date());