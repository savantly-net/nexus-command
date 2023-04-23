package domainapp.webapp.application.services.homepage.dom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HomepageVersionRepository extends JpaRepository<HomepageVersion, Long> {
    
    // Get the new published version
    @Query("SELECT h FROM HomepageVersion h WHERE h.published = true ORDER BY h.id DESC")
    List<HomepageVersion> findByPublished(boolean published);
}
