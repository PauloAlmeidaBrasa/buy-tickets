ALTER TABLE tickets
ADD COLUMN reserved_until TIMESTAMP NULL;

UPDATE tickets
SET status = 'AVAILABLE'
WHERE status IS NULL;

ALTER TABLE tickets
ADD CONSTRAINT chk_ticket_status
CHECK (status IN ('AVAILABLE', 'RESERVED', 'SOLD'));