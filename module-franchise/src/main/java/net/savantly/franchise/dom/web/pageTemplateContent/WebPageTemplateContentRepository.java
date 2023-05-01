package net.savantly.franchise.dom.web.pageTemplateContent;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebPageTemplateContentRepository extends JpaRepository<WebPageTemplateContent, Long> {

    Collection<WebPageTemplateContent> findByNameContainingIgnoreCase(String search);
    
}
