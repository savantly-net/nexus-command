package net.savantly.nexus.orgfees.dom.onetime;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OneTimePurchaseRepository extends JpaRepository<OneTimePurchase, String>{
    
    @Query("SELECT p FROM OneTimePurchase p WHERE p.organization.id = ?1 AND (p.endDate >= ?2 OR p.endDate IS NULL) AND p.startDate <= ?3")
    public List<OneTimePurchase> findByOrganizationIdAndDateRange(String organizationId, LocalDate startDate, LocalDate endDate);

    public Set<OneTimePurchase> findByOrganizationId(String id);
}
