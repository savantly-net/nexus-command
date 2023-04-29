package net.savantly.franchise.dom.brand;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandMemberRespository extends JpaRepository<BrandMember, Long> {
    
    Set<BrandMember> findByUserName(String username);
}
