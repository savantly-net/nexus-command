DO 'BEGIN
    IF to_regtype(''causewayExtCommandLog.clob'') IS NULL THEN
        create domain causewayExtCommandLog.clob as text;
    END IF;
END;
'  LANGUAGE PLPGSQL;