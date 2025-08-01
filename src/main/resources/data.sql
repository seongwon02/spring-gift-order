insert into product (name, price, image_url)
values ('사과 2kg', 14900, 'https://example1.png');

insert into product (name, price, image_url)
values ('카카오프랜즈 인형', 30000, 'https://example2.png');

insert into product (name, price, image_url)
values ('테스트 상품 1', 10000, 'https://example3.png');

insert into product (name, price, image_url)
values ('테스트 상품 2', 20000, 'https://example4.png');

insert into product (name, price, image_url)
values ('테스트 상품 3', 30000, 'https://example5.png');

insert into product (name, price, image_url)
values ('테스트 상품 4', 40000, 'https://example6.png');

insert into member (email, password, role)
values ('admin@example.com', '1234', 'ADMIN');

insert into member (email, password, role)
values ('user@example.com', '0000', 'USER');

insert into member (email, password, role)
values ('user2@example.com', '0000', 'USER');

insert into wish (member_id, product_id)
values (2, 1);

insert into wish (member_id, product_id)
values (3, 2);

insert into wish (member_id, product_id)
values (2, 3);

insert into wish (member_id, product_id)
values (2, 4);

insert into wish (member_id, product_id)
values (2, 5);

insert into wish (member_id, product_id)
values (2, 6);

insert into product_option (name, quantity, product_id)
values ('S size', 100, 2);

insert into product_option (name, quantity, product_id)
values ('M size', 100, 2);