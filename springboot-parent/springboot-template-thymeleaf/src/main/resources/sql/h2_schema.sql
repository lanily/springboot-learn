drop table if exists t_employee;
drop table if exists t_department;

create table if not exists t_department (
	id int not null auto_increment,
	name varchar(50),
	primary key(id)
);

create table if not exists t_employee (
	id int not null auto_increment,
	name varchar(50),
	gender int,
	birthday date,
	email varchar(50),
	dept_id int,
	primary key(id),
	foreign key(dept_id) references t_department(id)
);
