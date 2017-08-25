



insert into file_sharing_system.role (name) values ("ROLE_USER"), ("ROLE_ADMIN");



insert into file_sharing_system.user (email, login, firstName, lastName, password, phone, role_id) values ("user1@gmail.com", "user1", "User1firstname", "User1lastname", "12345qaz", "+1(111)-111-1111", 2);
insert into file_sharing_system.user (email, login, firstName, lastName, password, phone, role_id) values ("user2@gmail.com", "user2", "User2firstname", "User2lastname", "12345qaz", "+2(222)-222-2222", 1);
insert into file_sharing_system.user (email, login, firstName, lastName, password, phone, role_id) values ("user3@gmail.com", "user3", "User3firstname", "User3lastname", "12345qaz", "+2(222)-222-2222", 1);



insert into file_sharing_system.resource (name, parent_id, type) values ("audio", null, "folder");
insert into file_sharing_system.resource (name, parent_id, type) values ("video", null, "folder");
insert into file_sharing_system.resource (name, parent_id, type) values ("image", null, "folder");
insert into file_sharing_system.resource (name, parent_id, type) values ("beatles.mp3", 2, "file");

insert into file_sharing_system.resource_user (resources_id, users_id) values (1, 1);
insert into file_sharing_system.resource_user (resources_id, users_id) values (2, 1);
insert into file_sharing_system.resource_user (resources_id, users_id) values (3, 1);
insert into file_sharing_system.resource_user (resources_id, users_id) values (2, 2);
insert into file_sharing_system.resource_user (resources_id, users_id) values (3, 3);
insert into file_sharing_system.resource_user (resources_id, users_id) values (4, 1);
