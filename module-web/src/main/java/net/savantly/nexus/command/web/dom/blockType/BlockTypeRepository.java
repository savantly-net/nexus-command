package net.savantly.nexus.command.web.dom.blockType;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockTypeRepository extends JpaRepository<BlockType, String>{

    Collection<BlockType> findByNameContainingIgnoreCase(String search);
    
}
