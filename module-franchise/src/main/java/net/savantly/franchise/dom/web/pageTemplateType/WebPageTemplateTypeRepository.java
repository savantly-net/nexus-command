package net.savantly.franchise.dom.web.pageTemplateType;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebPageTemplateTypeRepository extends JpaRepository<WebPageTemplateType, Long> {

    Collection<WebPageTemplateType> findByNameContainingIgnoreCase(String search);
    
}
