-------------------------------------------------------------------------------------------------
-- create new table confirmation_token needed for verifying the user account via email.
-------------------------------------------------------------------------------------------------

create table confirmation_token(
token_id int primary key identity(1,1),
confirmation_token varchar(255),
created_date date,
user_account_id varchar(64) not null,
is_used bit default 0
);