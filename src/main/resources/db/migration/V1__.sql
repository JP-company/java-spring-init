CREATE TABLE account
(
    id         INT AUTO_INCREMENT NOT NULL,
    email      VARCHAR(255) NOT NULL,
    nickname   VARCHAR(10)  NOT NULL,
    status     VARCHAR(10)  NOT NULL,
    created_at datetime     NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

CREATE TABLE email_auth_code
(
    email       VARCHAR(50) NOT NULL,
    auth_code   VARCHAR(10) NOT NULL,
    expiry_time datetime    NOT NULL,
    CONSTRAINT pk_email_auth_code PRIMARY KEY (email)
);

CREATE TABLE email_limit
(
    email VARCHAR(50) NOT NULL,
    count SMALLINT NULL,
    date  date        NOT NULL,
    CONSTRAINT pk_email_limit PRIMARY KEY (email)
);

CREATE TABLE login_refresh_token
(
    account_id    INT          NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    expiry_time   datetime     NOT NULL,
    CONSTRAINT pk_login_refresh_token PRIMARY KEY (account_id)
);

ALTER TABLE account
    ADD CONSTRAINT uc_account_email UNIQUE (email);

ALTER TABLE login_refresh_token
    ADD CONSTRAINT uc_login_refresh_token_refresh_token UNIQUE (refresh_token);

CREATE INDEX idx_account_created_at ON account (created_at);

CREATE INDEX idx_account_name ON account (nickname);

CREATE INDEX idx_account_status ON account (status);

ALTER TABLE login_refresh_token
    ADD CONSTRAINT FK_LOGIN_REFRESH_TOKEN_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);