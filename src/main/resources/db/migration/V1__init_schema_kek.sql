-- Wir stellen sicher, dass Postgres UUIDs generieren kann
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE app_user (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          username VARCHAR(255) NOT NULL UNIQUE,
                          email VARCHAR(255)
);

CREATE TABLE track (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       title VARCHAR(255) NOT NULL,
                       artist VARCHAR(255),
                       user_id UUID, -- Muss jetzt auch UUID sein!
                       CONSTRAINT fk_track_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);