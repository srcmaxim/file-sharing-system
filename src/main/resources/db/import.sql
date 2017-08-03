



insert into file_sharing_system.role (name) values ("ROLE_USER"), ("ROLE_ADMIN");



insert into file_sharing_system.user (email, firstName, lastName, login, password, phone, role_id) values ("srcmaxim@gmail.com", "Maksim", "Koval", "srcmaxim", "12345qaz", "0671231568", 2);
insert into file_sharing_system.user (email, firstName, lastName, login, password, phone, role_id) values ("tima@gmail.com", "Timofey", "Koval", "tima", "12345qaz", "0671231438", 1);
insert into file_sharing_system.user (email, firstName, lastName, login, password, phone, role_id) values ("1below1@gmail.com", "Denis", "Below", "1below1", "12345qaz", "0671237733", 1);



insert into file_sharing_system.resource (name, parent_id, type) values ("audio", null, "folder");
insert into file_sharing_system.resource (name, parent_id, type) values ("video", null, "folder");
insert into file_sharing_system.resource (name, parent_id, type) values ("image", null, "folder");

insert into file_sharing_system.resource_user (resources_id, users_id) values (1, 1);
insert into file_sharing_system.resource_user (resources_id, users_id) values (2, 1);
insert into file_sharing_system.resource_user (resources_id, users_id) values (3, 1);
insert into file_sharing_system.resource_user (resources_id, users_id) values (2, 2);
insert into file_sharing_system.resource_user (resources_id, users_id) values (3, 3);
