package net.savantly.nexus.products.dom.productPrice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, String>{

    List<ProductPrice> findAllByProductId(String productId);
    
}
