package net.savantly.nexus.flow.dom.form;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<Form, String>{
        
        Set<Form> findByOrganizationId(String id);
}
