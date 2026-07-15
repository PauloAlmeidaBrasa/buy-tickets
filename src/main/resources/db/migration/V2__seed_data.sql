INSERT INTO event_catalog (id, name, event_date, event_address, tickets_remain)
VALUES
    (1, 'Rock Festival', '2026-08-15 20:00:00', 'Arena Central', 150),
    (2, 'Jazz Night', '2026-09-01 19:30:00', 'Blue Hall', 80),
    (3, 'Comedy Show', '2026-10-10 21:00:00', 'Moon Theater', 60)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    event_date = VALUES(event_date),
    event_address = VALUES(event_address),
    tickets_remain = VALUES(tickets_remain);

INSERT INTO tickets (id, event_id, status)
VALUES
    (1, 1, 'available'),
    (2, 1, 'sold'),
    (3, 2, 'available'),
    (4, 2, 'available'),
    (5, 3, 'available')
ON DUPLICATE KEY UPDATE
    event_id = VALUES(event_id),
    status = VALUES(status);
