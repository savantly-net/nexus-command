package net.savantly.nexus.projects.dom.projectMember;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    
    Set<ProjectMember> findByUsername(String username);
}
