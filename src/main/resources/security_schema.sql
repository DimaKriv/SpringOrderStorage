CREATE TABLE USERS (
                       username VARCHAR(255) NOT NULL PRIMARY KEY,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL,
                       first_name VARCHAR(255) NOT NULL
);

CREATE TABLE AUTHORITIES (
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             FOREIGN KEY (username) REFERENCES USERS
                                 ON DELETE CASCADE
);

INSERT INTO USERS(username, password,enabled, first_name) VALUES ('user'
, '$2y$12$jvqkC0tlmXg3fcH1UEm6P.lyalptFxPA3m4Nn1GgXPSH28g0rwF.S', true, 'USER');
INSERT INTO USERS(username, password,enabled, first_name) VALUES ('admin'
, '$2y$12$nljTHdiyZ7eLP7ZbcZAIoe3R23LvTS3ToLaHrxPiapDOT.nHYnTQu', true, 'ADMIN');

INSERT INTO AUTHORITIES (username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO AUTHORITIES (username, authority) VALUES ('user', 'ROLE_USER');
INSERT INTO AUTHORITIES (username, authority) VALUES ('admin', 'ROLE_USER');