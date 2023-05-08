package net.savantly.nexus.command.web.dom.block;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, String>{

    Collection<Block> findByNameContainingIgnoreCase(String search);
    
}
