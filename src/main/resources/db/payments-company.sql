-- Run manually after creating payments. Existing payments require an explicit
-- company mapping; this script does not infer a company from a user's current company.
SET XACT_ABORT ON;
BEGIN TRANSACTION;
IF COL_LENGTH('dbo.payments', 'company_id') IS NULL
BEGIN
    IF EXISTS (SELECT 1 FROM dbo.payments)
        THROW 50000, 'Existing payments must be mapped to their original company before adding company_id.', 1;

    ALTER TABLE dbo.payments ADD company_id INT NOT NULL;
    EXEC('ALTER TABLE dbo.payments ADD CONSTRAINT FK_payments_company
        FOREIGN KEY (company_id) REFERENCES dbo.companies(id)');
END;
COMMIT TRANSACTION;
