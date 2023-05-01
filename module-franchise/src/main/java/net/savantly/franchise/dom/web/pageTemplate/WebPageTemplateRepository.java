package net.savantly.franchise.dom.web.pageTemplate;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebPageTemplateRepository extends JpaRepository<WebPageTemplate, Long> {

    Collection<WebPageTemplate> findByNameContainingIgnoreCase(String search);
    
}
