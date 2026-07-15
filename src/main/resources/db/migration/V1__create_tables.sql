CREATE TABLE IF NOT EXISTS event_catalog (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    event_date DATETIME NOT NULL,
    event_address VARCHAR(255) NOT NULL,
    tickets_remain INT NOT NULL
);

CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_tickets_event FOREIGN KEY (event_id) REFERENCES event_catalog(id)
);
