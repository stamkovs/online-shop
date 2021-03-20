------------------------------------------------------------------------------------------------------------
-- create table product_size_quantity, insert values into it, and drop column quantity from product table
------------------------------------------------------------------------------------------------------------

create table product_size_quantity (
id int identity(1,1),
product_id int,
size varchar(8),
quantity int check (quantity >= 0) default 0,
constraint pk_product_size_quantity primary key (id),
constraint fk_product_size_quantity_to_product foreign key(product_id) references product(id)
);

insert into product_size_quantity (product_id, size, quantity)
select id, '45' as size, quantity from product where product_category_id=1;

insert into product_size_quantity (product_id, size, quantity)
select id, '41' as size, 2 as quantity from product where product_category_id=1;

insert into product_size_quantity (product_id, size, quantity)
select id, '38' as size, quantity from product where product_category_id=5

alter table product drop column quantity;