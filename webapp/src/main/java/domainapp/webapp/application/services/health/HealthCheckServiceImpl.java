package domainapp.webapp.application.services.health;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.services.health.Health;
import org.apache.causeway.applib.services.health.HealthCheckService;
import org.springframework.stereotype.Service;

import net.savantly.franchise.dom.location.FranchiseLocations;

@Service
@Named("domainapp.HealthCheckServiceImpl")
public class HealthCheckServiceImpl implements HealthCheckService {

    private final FranchiseLocations simpleObjects;

    @Inject
    public HealthCheckServiceImpl(FranchiseLocations simpleObjects) {
        this.simpleObjects = simpleObjects;
    }

    @Override
    public Health check() {
        try {
            simpleObjects.ping();
            return Health.ok();
        } catch (Exception ex) {
            return Health.error(ex);
        }
    }
}
