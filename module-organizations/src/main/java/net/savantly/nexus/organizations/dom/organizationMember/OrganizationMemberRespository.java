package net.savantly.nexus.organizations.dom.organizationMember;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationMemberRespository extends JpaRepository<OrganizationMember, Long> {
    
    Set<OrganizationMember> findByUserName(String username);
}
