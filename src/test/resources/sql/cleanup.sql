WITH accounts AS (
    SELECT id from account WHERE name in ('acc1', 'acc2', 'acc3')
),
deleted_transaction_audits AS (
    DELETE FROM transaction_audit WHERE transaction_id in (SELECT id FROM transaction WHERE debit_account_id in (SELECT id FROM accounts) or credit_account_id in (SELECT id FROM accounts))
),
deleted_transactions AS (
    DELETE FROM transaction WHERE debit_account_id in (SELECT id FROM accounts) or credit_account_id in (SELECT id FROM accounts)
),
deleted_account_audits AS (
    DELETE FROM account_audit WHERE account_id in (SELECT id FROM accounts)
)
DELETE FROM account WHERE id in (SELECT id FROM accounts);
