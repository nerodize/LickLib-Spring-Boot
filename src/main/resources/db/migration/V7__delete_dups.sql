DELETE FROM track
WHERE description IS NULL
   OR description = '';