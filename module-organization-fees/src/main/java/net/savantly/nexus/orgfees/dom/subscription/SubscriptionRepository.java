package net.savantly.nexus.orgfees.dom.subscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    Set<Subscription> findByOrganizationId(String id);

    @Query("SELECT p FROM Subscription p WHERE p.organization.id = ?1 AND (p.endDate >= ?2 OR p.endDate IS NULL) AND p.startDate <= ?3")
    public List<Subscription> findByOrganizationIdAndDateRange(String organizationId, LocalDate startDate,
            LocalDate endDate);

}
