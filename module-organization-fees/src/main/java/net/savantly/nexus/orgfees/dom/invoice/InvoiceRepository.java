package net.savantly.nexus.orgfees.dom.invoice;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, String>{

    Set<Invoice> findByOrganizationId(String id);
    
}
