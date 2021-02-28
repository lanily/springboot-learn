use demo;

insert into t_department(name) values('部门一');
insert into t_department(name) values('部门二');
insert into t_department(name) values('部门三');

insert into t_employee(name, gender, birthday, email, dept_id)
values ('赵波', 1, '1986-05-29', 'codergege@163.com', 1);
insert into t_employee(name, gender, birthday, email, dept_id)
values ('djh', 0, '1988-03-22', 'codergege@163.com', 2);
insert into t_employee(name, gender, birthday, email, dept_id)
values ('aa', 1, '1986-05-22', 'codergege@163.com', 3);
