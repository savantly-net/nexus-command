DO $$ BEGIN
    create domain clob as text;
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;
