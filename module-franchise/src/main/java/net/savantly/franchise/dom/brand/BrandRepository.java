package net.savantly.franchise.dom.brand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, String>{

    List<Brand> findByNameContainingIgnoreCase(String search);

    Brand findByName(String name);

    Brand findByNameIgnoreCase(String name);
    
}
