-------------------------------------------------------------------------------------------------
-- alters table user_account to drop the age column, and add account_id column
-------------------------------------------------------------------------------------------------

alter table user_account drop column age;

alter table user_account add account_id varchar(64) unique;