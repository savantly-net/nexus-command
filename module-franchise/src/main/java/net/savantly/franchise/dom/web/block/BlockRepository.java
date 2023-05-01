package net.savantly.franchise.dom.web.block;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long>{

    Collection<Block> findByNameContainingIgnoreCase(String search);
    
}
