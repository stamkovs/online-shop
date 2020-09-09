create table user_account(
id int primary key,
email varchar(50) unique,
password varchar(255) not null,
first_name varchar(30) not null,
last_name varchar(30) not null,
age int
)