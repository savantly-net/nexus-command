package net.savantly.nexus.flow.dom.connections.jdbcConnection;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JdbcConnectionRepository extends JpaRepository<JdbcConnection, String> {

    Collection<JdbcConnection> findByOrganizationId(String organizationId);

}
