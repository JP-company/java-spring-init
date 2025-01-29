CREATE TABLE account
(
    id         INT AUTO_INCREMENT NOT NULL,
    address    VARCHAR(50) NOT NULL,
    status     VARCHAR(20) NOT NULL,
    created_at datetime    NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

ALTER TABLE account
    ADD CONSTRAINT uc_account_address UNIQUE (address);

CREATE INDEX idx_account ON account (created_at);

CREATE INDEX idx_account_status ON account (status);