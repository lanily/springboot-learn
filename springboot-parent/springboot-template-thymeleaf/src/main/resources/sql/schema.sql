use demo;

drop table if exists t_employee;
drop table if exists t_department;

create table if not exists t_department (
	id int(10) not null auto_increment,
	name varchar(50),
	index(id),
	primary key(id)
) engine innodb default character set utf8;

create table if not exists t_employee (
	id int(10) not null auto_increment,
	name varchar(50),
	gender int(1),
	birthday date,
	email varchar(50),
	dept_id int(10),
	index(id, dept_id),
	primary key(id),
	foreign key(dept_id) references t_department(id)
) engine innodb default character set utf8;
