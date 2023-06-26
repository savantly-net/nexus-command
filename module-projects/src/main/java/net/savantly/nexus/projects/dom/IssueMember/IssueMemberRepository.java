package net.savantly.nexus.projects.dom.IssueMember;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueMemberRepository extends JpaRepository<IssueMember, Long> {
    
    Set<IssueMember> findByUserName(String username);
}
