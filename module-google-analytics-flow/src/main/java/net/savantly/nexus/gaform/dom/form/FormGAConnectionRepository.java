package net.savantly.nexus.gaform.dom.form;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FormGAConnectionRepository extends JpaRepository<FormGAConnection, String> {

    FormGAConnection findByFormId(String id);

    void deleteByFormId(String id);

}
