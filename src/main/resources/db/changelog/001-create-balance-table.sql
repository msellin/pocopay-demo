CREATE OR REPLACE FUNCTION trigger_create_account_audit()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO account_audit (account_id, name, balance, action, created_date, created_session_id) VALUES (NEW.id, NEW.name, NEW.balance, TG_ARGV[0], clock_timestamp(), NEW.modified_session_id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION trigger_create_transaction_audit()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO transaction_audit (transaction_id, debit_account_id, credit_account_id, amount, created_date, created_session_id) VALUES (NEW.id, NEW.debit_account_id, NEW.credit_account_id, NEW.amount, clock_timestamp(), NEW.created_session_id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE account
(
    id          bigserial NOT NULL PRIMARY KEY,
    name          text NOT NULL,
    balance      numeric(10, 2) NOT NULL,
    created_session_id  text not null,
    modified_session_id text not null
);

CREATE TABLE account_audit
(
    id          bigserial NOT NULL PRIMARY KEY,
    name          text NOT NULL,
    account_id    bigint NOT NULL REFERENCES account(id),
    balance      numeric(10, 2) NOT NULL,
    action      text NOT NULL,
    created_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_session_id  text not null
);

CREATE TABLE transaction
(
    id          bigserial NOT NULL PRIMARY KEY,
    debit_account_id    bigint NOT NULL REFERENCES account(id),
    credit_account_id    bigint NOT NULL REFERENCES account(id),
    amount      numeric(10, 2) NOT NULL,
    created_session_id  text not null
);

CREATE TABLE transaction_audit
(
    id          bigserial NOT NULL PRIMARY KEY,
    transaction_id    bigint NOT NULL REFERENCES transaction(id),
    debit_account_id    bigint NOT NULL REFERENCES account(id),
    credit_account_id    bigint NOT NULL REFERENCES account(id),
    amount      numeric(10, 2) NOT NULL,
    created_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_session_id  text not null
);

CREATE TRIGGER account_create_account_audit
    AFTER INSERT ON account
    FOR EACH ROW
EXECUTE PROCEDURE trigger_create_account_audit('INSERT');

CREATE TRIGGER account_update_account_audit
    AFTER UPDATE ON account
    FOR EACH ROW
EXECUTE PROCEDURE trigger_create_account_audit('UPDATE');

CREATE TRIGGER transaction_create_transaction_audit
    AFTER INSERT ON transaction
    FOR EACH ROW
EXECUTE PROCEDURE trigger_create_transaction_audit();
