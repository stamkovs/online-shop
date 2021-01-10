-------------------------------------------------------------------------------------------------
-- Alters the user_account table by adding new columns email_verified, provider, provider_id
-- needed for the oauth2 sign in with providers
-- made password nullable if user logs in via google or facebook
-------------------------------------------------------------------------------------------------

alter table user_account add email_verified bit not null default 0;
alter table user_account add provider varchar(12) not null default 'local';
alter table user_account add provider_id varchar(64);
alter table user_account alter column password varchar(255) null;