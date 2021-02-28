-------------------------------------------------------------------------------------------------
-- insert admin account into user_account table.
-------------------------------------------------------------------------------------------------

insert into user_account (email, password, first_name, last_name, user_role_id, email_verified, provider, account_id)
values ('shoptasticmk@gmail.com', '$2a$13$9QAA.UdbEmRggdP4o7O6K.IqqWSTMBoWjrtd2cHaW4R.I9jr9AeJy', 'shoptasticAdmin', 'shoptasticAdmin',
1, 1, 'local', NEWID()
);