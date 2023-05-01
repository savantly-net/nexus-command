package net.savantly.franchise.dom.web.site;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebSiteRepository extends JpaRepository<WebSite, Long> {

    Collection<WebSite> findByNameContainingIgnoreCase(String search);
    
}
