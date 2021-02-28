-- create table user_role first since it is being referenced in the user_account
create table user_role(
id int identity(1, 1),
name varchar(15),
permission varchar(15),
constraint pk_user_role primary key (id)
);

create table user_account(
id int identity(1, 1),
email varchar(50) unique,
password varchar(255) not null,
first_name varchar(30) not null,
last_name varchar(30) not null,
age int,
user_role_id int not null,
constraint pk_user_account primary key (id),
constraint fk_user_account_role_to_user_role foreign key(user_role_id) references user_role(id)
);

create table product_category(
id int identity(1, 1),
category varchar(30),
description varchar(128),
constraint pk_product_category primary key (id)
);

create table product(
id int identity(1, 1),
product_category_id int,
user_account_id int not null,
name varchar(64),
description varchar(1024),
image_url varchar(2048),
price decimal(6,2),
quantity int,
created_on DATETIME,
updated_on DATETIME,
constraint pk_product primary key (id),
constraint fk_product_to_product_category foreign key (product_category_id) references product_category(id),
constraint fk_product_to_user_account foreign key (user_account_id) references user_account(id)
);

create table user_order(
id int identity(1, 1),
user_account_id int not null,
created_on DATETIME,
delivery_address varchar(255),
total_price decimal(2, 2),
comment varchar(255),
total_items int
constraint pk_order primary key (id),
constraint fk_order_to_user_account foreign key (user_account_id) references user_account(id)
);

create table order_details(
order_id int,
product_id int,
quantity int,
price decimal(2, 2),
constraint pk_order_details primary key (order_id, product_id),
constraint fk_order_details_to_order foreign key (order_id) references user_order(id),
constraint fk_order_details_to_product foreign key (product_id) references product(id),
);

create table product_wishlist(
user_account_id int,
product_id int,
created_at DATETIME,
updated_at DATETIME,
constraint pk_product_wishlist primary key (user_account_id),
constraint fk_product_wishlist_to_user_account foreign key (user_account_id) references user_account(id),
constraint fk_product_wishlist_to_product foreign key (product_id) references product(id)
);

create table user_card_details(
user_account_id int,
card_number varchar(255),
valid_until date,
constraint pk_user_card_details primary key (user_account_id),
constraint fk_user_card_details_to_user_account foreign key (user_account_id) references user_account(id)
);

