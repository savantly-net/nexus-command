package net.savantly.franchise.dom.web.blockType;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockTypeRepository extends JpaRepository<BlockType, Long>{

    Collection<BlockType> findByNameContainingIgnoreCase(String search);
    
}
