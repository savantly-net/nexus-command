package net.savantly.nexus.command.web.dom.page;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebPageRepository extends JpaRepository<WebPage, String> {

    Collection<WebPage> findByNameContainingIgnoreCase(String search);
    
}
