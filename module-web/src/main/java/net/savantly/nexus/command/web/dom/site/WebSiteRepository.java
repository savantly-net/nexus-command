package net.savantly.nexus.command.web.dom.site;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebSiteRepository extends JpaRepository<WebSite, String> {

    Collection<WebSite> findByNameContainingIgnoreCase(String search);
    
}
