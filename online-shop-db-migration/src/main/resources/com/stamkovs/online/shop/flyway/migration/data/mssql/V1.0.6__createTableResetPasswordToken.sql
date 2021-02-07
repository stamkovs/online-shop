-------------------------------------------------------------------------------------------------
-- create new table reset_password_token needed to reset password for an account by sending email.
-------------------------------------------------------------------------------------------------

create table reset_password_token(
token_id int primary key identity(1,1),
reset_password_token varchar(255),
created_date DATETIME,
user_account_id varchar(64) not null,
is_used bit default 0
);