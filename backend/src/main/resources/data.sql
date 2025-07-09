INSERT INTO users (name)
SELECT 'ivan'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE name = 'ivan'
);