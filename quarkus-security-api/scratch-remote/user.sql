select * from user;

INSERT INTO user (username, password, roles) VALUES ('ADMIN', '$2a$12$53pCKgi8njRmX56hVq2gT.gm/b.LlJbh0sAxWFUyG5fpc7Bg7HlNC', 'MANAGER,CUSTOMER');
INSERT INTO user (username, password, roles) VALUES ('VENDOR', '$2a$12$t8tn15mxSv2k3EcnmBBkROPwDmafkhz90LFk3NtFLLXIaZO8Pz6LK', 'CUSTOMER');
update user set roles='MANAGER,CUSTOMER' where id =1;
update user set username='VENDOR',roles='CUSTOMER' where id =2;