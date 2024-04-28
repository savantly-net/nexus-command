package domainapp.webapp.application.services.health;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.services.health.Health;
import org.apache.causeway.applib.services.health.HealthCheckService;
import org.springframework.stereotype.Service;

@Service
@Named("domainapp.HealthCheckServiceImpl")
public class HealthCheckServiceImpl implements HealthCheckService {

    @Inject
    public HealthCheckServiceImpl() {
    }

    @Override
    public Health check() {
        try {
            return Health.ok();
        } catch (Exception ex) {
            return Health.error(ex);
        }
    }
}
