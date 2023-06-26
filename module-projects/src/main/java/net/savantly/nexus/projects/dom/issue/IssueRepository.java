package net.savantly.nexus.projects.dom.issue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, String>{

    List<Issue> findByNameContainingIgnoreCase(String search);

    Issue findByName(String name);

    Issue findByNameIgnoreCase(String name);
    
}
