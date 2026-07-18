ALTER TABLE tickets
ADD COLUMN user_id BIGINT NULL;

ALTER TABLE tickets
ADD CONSTRAINT fk_tickets_user
FOREIGN KEY (user_id) REFERENCES users(id);