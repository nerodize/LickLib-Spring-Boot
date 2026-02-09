-- Erst einen User anlegen, damit der Foreign Key bedient werden kann
INSERT INTO app_user (id, username, email)
VALUES ('d290f1ee-6c54-4b01-90e6-d701748f0851', 'Alex', 'alex@example.com');




-- Tracks hinzufügen
INSERT INTO track (title, artist, user_id)
VALUES ('E-Minor Lick', 'Paul Gilbert', 'd290f1ee-6c54-4b01-90e6-d701748f0851');

INSERT INTO track (title, artist, user_id)
VALUES ('Blues Shuffle', 'Stevie Ray Vaughan', 'd290f1ee-6c54-4b01-90e6-d701748f0851');