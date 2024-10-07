CREATE TABLE employee (
  employee_id VARCHAR(9) PRIMARY KEY,
  date_of_birth DATE NOT NULL,
  first_name VARCHAR(20) NOT NULL,
  last_name VARCHAR(20) NOT NULL,
  middle_name VARCHAR(20) NOT NULL,
  salary INT DEFAULT NULL
);

ALTER TABLE employee
  ADD CONSTRAINT fk_department
  FOREIGN KEY (deptid) REFERENCES department(departmentid)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE employee
  ADD COLUMN gender VARCHAR(10) DEFAULT NULL;

CREATE SEQUENCE department_seq
    START 1
  INCREMENT 1
  MINVALUE 1
  OWNED BY department.departmentid;

CREATE SEQUENCE department_location_seq
    START 1
  INCREMENT 1
  MINVALUE 1
  OWNED BY department_location.id;

alter table department
    rename column departmentid to department_id;

create table project (
 project_id bigserial primary key,
 area varchar(255) not null,
 name varchar(255) unique not null,
 managed_department_id bigint not null,
 constraint fk_department
     foreign key (managed_department_id) references department(department_id)
         on delete cascade
         on update cascade
);

alter table project
    rename column name to project_name;

create table assignment (
                          assignment_id bigserial primary key,
                          number_of_hours int default 0,
                          employee_assigned_id bigint not null
);

create sequence assignment_seq
  start 1
  increment 1
  minvalue 1
  owned by assignment.assignment_id;

alter table assignment
alter column employee_assigned_id TYPE varchar(10);

alter table assignment
  add constraint fk_project
    foreign key (project_belonging_id) references project(project_id);

alter table "assignment"
  add constraint fk_employee
    foreign key (employee_assigned_id) references employee(employee_id)
      on update cascade
      on delete cascade;

create table "user" (
                      username varchar(255) primary key,
                      email varchar(255) unique not null,
                      "password" varchar(255) not null
);

alter table "user"
  add column "role" varchar(255) not null;

create table role (
                    role_id serial primary key,
                    "name" varchar(255) unique not null
);

CREATE SEQUENCE role_seq
  START 1
  INCREMENT 1
  MINVALUE 1
  OWNED BY role.role_id;