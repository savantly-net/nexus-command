package net.savantly.nexus.franchise.dom.group;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseGroupRepository extends JpaRepository<FranchiseGroup, Long> {

	Collection<FranchiseGroup> findByNameContainingIgnoreCase(String search);

    Set<FranchiseGroup> findByMembersUserName(String username);

}
