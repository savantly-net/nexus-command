package net.savantly.franchise.dom.brand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long>{

    List<Brand> findByNameContainingIgnoreCase(String search);
    
}
