-- create schema onthionline;
-- use onthionline;

-- UPDATE user
-- SET created_date = CURRENT_TIMESTAMP(), last_login = CURRENT_TIMESTAMP()
-- WHERE id = 1;

-- UPDATE role
-- SET created_date = CURRENT_TIMESTAMP(), updated_date = CURRENT_TIMESTAMP()
-- WHERE id in (1,2,3);

-- create table test2 (
-- 	id int(11) not null primary key auto_increment,
--     created_date timestamp not null 
-- );
create table user (
	id int(11) not null primary key auto_increment,
    user_name varchar(50) not null,
    full_name varchar(255) not null COLLATE utf8_unicode_ci,
    email varchar(50) not null,
    password varchar(100) not null,
	phone varchar(30),
    avatar varchar(255),
    birthday timestamp,
    gender varchar(5) default 'nam',
    city varchar(20) COLLATE utf8_unicode_ci,
    class varchar(5)COLLATE utf8_unicode_ci,
    school varchar(50) COLLATE utf8_unicode_ci,
    is_active boolean not null,
    online_time int(255) default 0,
    created_date timestamp not null default current_timestamp,
	last_login timestamp default current_timestamp
);

create table role (
	id int(11) not null primary key auto_increment,
    role_name varchar(50) not null,
	description varchar(100) not null COLLATE utf8_unicode_ci,
    created_date timestamp not null default current_timestamp,
    updated_date timestamp not null default current_timestamp
);

create table user_role (
    user_id int(11) not null,
    role_id int(11) not null
);

create table exam (
	id int(11) not null primary key auto_increment,
    name varchar(255) not null COLLATE utf8_unicode_ci,
    code varchar(50) not null COLLATE utf8_unicode_ci,
    subject varchar(50) not null COLLATE utf8_unicode_ci,
    grade varchar(50) not null COLLATE utf8_unicode_ci,
    num_question int(5) not null,
    num_people_did int(5) not null,
    description text COLLATE utf8_unicode_ci,
    mixed_question boolean not null,
    is_active boolean not null,
    time int(10),
    user_created varchar(255) not null,
    created_date timestamp not null default current_timestamp,
    updated_date timestamp not null default current_timestamp
);

/*create table part_exam (
	id int(11) not null primary key auto_increment,
	exam_id int(11) not null,
    name varchar(255) not null,
    description text,
    created_date timestamp not null default current_timestamp,
    updated_date timestamp not null default current_timestamp
);*/

/*create table part_question (
	part_id int(11) not null,
	question_id int(11) not null
);*/

create table question (
	id int(11) not null primary key auto_increment,
    question text not null COLLATE utf8_unicode_ci,
    type varchar(10) not null COLLATE utf8_unicode_ci,   /*one: chọn 1, multi: chọn nhiều*/  
    option1 text COLLATE utf8_unicode_ci,
    option2 text COLLATE utf8_unicode_ci,
    option3 text COLLATE utf8_unicode_ci,
    option4 text COLLATE utf8_unicode_ci,
    mode varchar(20) COLLATE utf8_unicode_ci,
    subject varchar(50) not null COLLATE utf8_unicode_ci,
    grade varchar(50) not null COLLATE utf8_unicode_ci,
    suggestion text COLLATE utf8_unicode_ci,
    correct_answer text COLLATE utf8_unicode_ci,
    /* part_id int(11) not null, */
    created_date timestamp not null default current_timestamp,
    updated_date timestamp default current_timestamp
);

create table exam_question (
	exam_id int(11) not null,
	question_id int(11) not null
);


create table exam_history (
	id int(11) not null primary key auto_increment,
	user_id int(11) not null,
    exam_id int(11) not null,
    created_date timestamp not null default current_timestamp,
    num_option_picked varchar(50) not null,
    num_correct_ans int(5) not null ,
    num_ans int(5) not null,
    exam_answer longblob not null,
    time int(10)
);

create table comment (
	id int(11) not null primary key auto_increment,
    user_id int(11) not null,
    exam_id int(11) not null,
    content text not null COLLATE utf8_unicode_ci,
    users_liked text COLLATE utf8_unicode_ci,
    parent_id int(11),
    created_date timestamp not null default current_timestamp,
    updated_date timestamp default current_timestamp
);

alter table user_role add constraint fk_userid foreign key (user_id) references user(id);
alter table user_role add constraint fk_roleid foreign key (role_id) references role(id);
alter table exam_history add constraint fk_user_history foreign key (user_id) references user(id);
alter table exam_history add constraint fk_exam_history foreign key (exam_id) references exam(id);
alter table exam_question add constraint fk_exam foreign key (exam_id) references exam(id);
alter table exam_question add constraint fk_question foreign key (question_id) references question(id);
alter table comment add constraint fk_exam_comment foreign key (exam_id) references exam(id);
alter table comment add constraint fk_user_comment foreign key (user_id) references user(id);



insert into role(role_name, description) values('ROLE_ADMIN','ADMIN');
insert into role(role_name, description) values('ROLE_MODIFIED','MODIFIED');
insert into role(role_name, description) values('ROLE_USER','USER');
insert into user(user_name, full_name, email, phone, password, is_active) values('admin','Admin siêu cute','admin@luyenthi.vn', '012345678', '$2a$10$4Lmv5ybcj/enBpNB3UYYBOI4nBVA1YxXFrhimUSeKBSuRX73CL0OW', true);
insert into user_role(user_id,role_id) values(1,1);
