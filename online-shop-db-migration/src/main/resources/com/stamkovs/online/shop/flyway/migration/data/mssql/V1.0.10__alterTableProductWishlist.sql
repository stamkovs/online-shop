------------------------------------------------------------------------------------------------------------
-- alters table product_wishlist to drop the created_at and updated_at columns,
-- and add new column added_at, also change the primary key to composite from user_account_id and product_id
------------------------------------------------------------------------------------------------------------

alter table product_wishlist drop column created_at, updated_at;
alter table product_wishlist add added_at DATETIME;
alter table product_wishlist alter column user_account_id int not null;
alter table product_wishlist alter column product_id int not null;

alter table product_wishlist drop constraint pk_product_wishlist;
alter table product_wishlist add constraint pk_product_wishlist primary key (user_account_id, product_id);