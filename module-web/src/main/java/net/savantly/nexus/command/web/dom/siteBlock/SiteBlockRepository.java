package net.savantly.nexus.command.web.dom.siteBlock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteBlockRepository extends JpaRepository<SiteBlock, String> {

    List<SiteBlock> findBySiteId(String siteId);
    
}
