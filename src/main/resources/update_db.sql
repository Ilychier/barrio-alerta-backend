-- Migration SQL script: Add password column to users table
-- Target Database: PostgreSQL

-- Step 1: Add the column as nullable first to prevent failures if there are existing rows
ALTER TABLE users ADD COLUMN IF NOT EXISTS password VARCHAR(255);

-- Step 2: (Optional) Set a default temporary password for existing users if any exist.
-- The hash below is a BCrypt hash for: 'ChangeMe123!'
UPDATE users 
SET password = '$2a$10$oRbh2Xy6yHlK41pY1Z.8uudgWpW9bUaJmU0R/U8lR4x86X2q.6yvG' 
WHERE password IS NULL;

-- Step 3: Enforce the NOT NULL constraint now that all rows have a value
ALTER TABLE users ALTER COLUMN password SET NOT NULL;
