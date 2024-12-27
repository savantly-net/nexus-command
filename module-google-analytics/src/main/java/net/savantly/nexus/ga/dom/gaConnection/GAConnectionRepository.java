package net.savantly.nexus.ga.dom.gaConnection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GAConnectionRepository extends JpaRepository<GAConnection, String> {

    List<GAConnection> findByOrganizationId(String id);

}
