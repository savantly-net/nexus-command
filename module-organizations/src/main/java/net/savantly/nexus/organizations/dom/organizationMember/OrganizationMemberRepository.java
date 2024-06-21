package net.savantly.nexus.organizations.dom.organizationMember;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {
    
    Set<OrganizationMember> findByUsername(String username);

    Set<OrganizationMember> findByUsernameAndOrganizationId(String username, String id);
}
