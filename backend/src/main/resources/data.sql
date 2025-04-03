CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE short_urls (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    original_url TEXT NOT NULL,
    short_code VARCHAR(10) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    access_count INT DEFAULT 0
);

