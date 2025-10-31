-- Enable pgcrypto for gen_random_uuid() and crypt()
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Create a users table
CREATE TABLE IF NOT EXISTS app_users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  username TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL,
  role TEXT NOT NULL DEFAULT 'USER',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Insert admin user (password: adminpass) - will not overwrite if already present
INSERT INTO app_users (username, password, role)
VALUES ('admin', crypt('adminpass', gen_salt('bf')), 'ADMIN')
ON CONFLICT (username) DO NOTHING;
