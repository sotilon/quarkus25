CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(50) NOT NULL
);

-- Insert sample users
-- INSERT INTO user (username, password, roles) VALUES ('ADMIN', 'admin123', 'writer');
-- INSERT INTO user (username, password, roles) VALUES ('CLIENT', 'user123', 'reader');

INSERT INTO user (username, password, roles) VALUES ('ADMIN', '$2a$12$53pCKgi8njRmX56hVq2gT.gm/b.LlJbh0sAxWFUyG5fpc7Bg7HlNC', 'MANAGER,CUSTOMER');
INSERT INTO user (username, password, roles) VALUES ('VENDOR', '$2a$12$t8tn15mxSv2k3EcnmBBkROPwDmafkhz90LFk3NtFLLXIaZO8Pz6LK', 'CUSTOMER');
