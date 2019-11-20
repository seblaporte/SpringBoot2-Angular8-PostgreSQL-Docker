drop table IF EXISTS employees;

create table employees
(
  id            IDENTITY not null
                    constraint employee_pk primary key,
  email_address varchar(255) not null,
  first_name    varchar(255) not null,
  last_name     varchar(255) not null
);