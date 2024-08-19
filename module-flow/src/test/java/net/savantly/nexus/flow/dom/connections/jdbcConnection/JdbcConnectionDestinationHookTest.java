package net.savantly.nexus.flow.dom.connections.jdbcConnection;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

public class JdbcConnectionDestinationHookTest {

    @Test
    public void testTimestampString() {
        var ts = ZonedDateTime.now();

        Timestamp.valueOf(ts.toString());
    }
}
