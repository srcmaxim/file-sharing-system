
insert into file_sharing_system.user (login, password, firstName, lastName, email, phone) values ("user1", "12345qaz", "Useronename", "Useronename", "user1@gmail.com", "+1(111)-111-1111");
insert into file_sharing_system.user (login, password, firstName, lastName, email, phone) values ("user2", "12345qaz", "Usertwoname", "Usertwoname", "user2@gmail.com", "+2(222)-222-2222");
insert into file_sharing_system.user (login, password, firstName, lastName, email, phone) values ("user3", "12345qaz", "Userthreename", "Userthreename", "user3@gmail.com", "+3(333)-333-3333");

-- Default hashed password decoded with StandardPasswordEncoder(server.secret) for all accounts: 12345qaz --
UPDATE `file_sharing_system`.`user` SET `password`='134ee155783e6cc4f51a4206d7eaae3acc073a9821e74dc391d37424cd31cc0e4186c5a9fcfb6361' WHERE `id`='1';
UPDATE `file_sharing_system`.`user` SET `password`='134ee155783e6cc4f51a4206d7eaae3acc073a9821e74dc391d37424cd31cc0e4186c5a9fcfb6361' WHERE `id`='2';
UPDATE `file_sharing_system`.`user` SET `password`='134ee155783e6cc4f51a4206d7eaae3acc073a9821e74dc391d37424cd31cc0e4186c5a9fcfb6361' WHERE `id`='3';

insert into file_sharing_system.authority (user_id, name) values (1, "ROLE_USER");
insert into file_sharing_system.authority (user_id, name) values (2, "ROLE_USER");
insert into file_sharing_system.authority (user_id, name) values (3, "ROLE_USER");
insert into file_sharing_system.authority (user_id, name) values (3, "ROLE_ADMIN");

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
