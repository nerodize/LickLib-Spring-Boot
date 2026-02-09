
-- Neue Spalten hinzufügen
ALTER TABLE track ADD COLUMN description TEXT;
ALTER TABLE track ADD COLUMN size INTEGER DEFAULT 0;
ALTER TABLE track ADD COLUMN duration INTEGER DEFAULT 0;

-- Falls du sicherstellen willst, dass size/duration nicht null sind:
ALTER TABLE track ALTER COLUMN size SET NOT NULL;
ALTER TABLE track ALTER COLUMN duration SET NOT NULL;


INSERT INTO track (id, title, description, artist, user_id, size, duration) VALUES
-- Tracks für 'nero'
(gen_random_uuid(), 'E-Minor Lick', 'Fast alternate picking run', 'Paul Gilbert', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 1024, 12),
(gen_random_uuid(), 'Blues Shuffle', 'Classic Texas shuffle feel', 'Stevie Ray Vaughan', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 2048, 45),

-- Track für 'kira'
(gen_random_uuid(), 'Jazz Fusion Run', 'Legato sequence with outside notes', 'Allan Holdsworth', '550e8400-e29b-41d4-a716-446655440000', 512, 8),

-- Track für 'atlas'
(gen_random_uuid(), 'Pentatonic Burn', 'Aggressive pinch harmonics', 'Zakk Wylde', '123e4567-e89b-12d3-a456-426614174000', 1500, 20);
