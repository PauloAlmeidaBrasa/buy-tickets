CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
);

-- CREATE TABLE IF NOT EXISTS tickets (
--     id BIGINT PRIMARY KEY,
--     event_id BIGINT NOT NULL,
--     status VARCHAR(50) NOT NULL,
--     CONSTRAINT fk_tickets_event FOREIGN KEY (event_id) REFERENCES event_catalog(id)
-- );
