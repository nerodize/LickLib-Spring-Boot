

-- 1. Zuerst die User anlegen (Basis-Daten)
INSERT INTO app_user (id, username, email) VALUES
                                               ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'nero', 'nero@example.com'),
                                               ('550e8400-e29b-41d4-a716-446655440000', 'kira', 'kira@example.com'),
                                               ('123e4567-e89b-12d3-a456-426614174000', 'atlas', 'atlas@example.com');

-- 2. Dann die Tracks hinzufügen (referenzieren die IDs von oben)
INSERT INTO track (id, title, artist, user_id) VALUES
-- Tracks für 'nero'
(gen_random_uuid(), 'E-Minor Lick', 'Paul Gilbert', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
(gen_random_uuid(), 'Blues Shuffle', 'Stevie Ray Vaughan', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),

-- Track für 'kira'
(gen_random_uuid(), 'Jazz Fusion Run', 'Allan Holdsworth', '550e8400-e29b-41d4-a716-446655440000'),

-- Track für 'atlas'
(gen_random_uuid(), 'Pentatonic Burn', 'Zakk Wylde', '123e4567-e89b-12d3-a456-426614174000');