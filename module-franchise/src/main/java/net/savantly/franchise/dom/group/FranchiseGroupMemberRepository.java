package net.savantly.franchise.dom.group;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseGroupMemberRepository extends JpaRepository<FranchiseGroupMember, Long> {


    Set<FranchiseGroupMember> findByUserName(String username);
}
