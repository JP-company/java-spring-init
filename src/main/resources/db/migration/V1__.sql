CREATE TABLE account
(
    id              INT AUTO_INCREMENT NOT NULL,
    name            VARCHAR(10)  NOT NULL,
    phone_number    VARCHAR(20)  NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    status          VARCHAR(10)  NOT NULL,
    created_at      datetime     NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

ALTER TABLE account
    ADD CONSTRAINT uc_account_phone_number UNIQUE (phone_number);

CREATE INDEX idx_account_created_at ON account (created_at);

CREATE INDEX idx_account_name ON account (name);

CREATE INDEX idx_account_status ON account (status);