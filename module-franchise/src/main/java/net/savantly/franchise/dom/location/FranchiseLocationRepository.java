package net.savantly.franchise.dom.location;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FranchiseLocationRepository extends JpaRepository<FranchiseLocation, Long> {

    List<FranchiseLocation> findByNameContainingIgnoreCase(String search);
    
    Set<FranchiseLocation> findByMembersUserName(String username);

    @Query("select max(l.id) from FranchiseLocation l")
	Optional<Long> getMaxId();

}
