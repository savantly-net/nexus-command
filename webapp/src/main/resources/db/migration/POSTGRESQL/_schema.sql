DO 'BEGIN
    IF to_regtype(''clob'') IS NULL THEN
        create domain clob as text;
    END IF;
END;
'  LANGUAGE PLPGSQL;