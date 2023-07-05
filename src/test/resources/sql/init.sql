-- risk factor
WITH acc1 AS (
    INSERT INTO account (name, balance, created_session_id, modified_session_id) VALUES ('acc1', 0, 's1', 's1') RETURNING id
),

acc2 AS (
    INSERT INTO account (name, balance, created_session_id, modified_session_id) VALUES ('acc2', 500, 's2', 's2') RETURNING id
)

INSERT INTO transaction (credit_account_id, debit_account_id, amount, created_session_id) SELECT acc2.id, acc1.id, 50, 's3' FROM acc1, acc2 RETURNING id;

