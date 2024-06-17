package net.savantly.nexus.flow.dom.formSubmission;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FormSubmissionRepository extends JpaRepository<FormSubmission, String>{

    Set<FormSubmission> findByFormId(String id);

    
} 