package net.savantly.nexus.organizations.dom.organization;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationMemberRespository extends JpaRepository<OrganizationMember, Long> {
    
    Set<OrganizationMember> findByUserName(String username);
}
