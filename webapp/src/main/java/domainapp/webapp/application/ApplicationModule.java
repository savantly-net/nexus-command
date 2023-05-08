package domainapp.webapp.application;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import net.savantly.franchise.FranchiseModule;
import net.savantly.nexus.command.web.NexusCommandWebModule;

@Configuration
@Import({ FranchiseModule.class, NexusCommandWebModule.class })
@ComponentScan
@EnableJpaRepositories
@EntityScan
public class ApplicationModule {

    public static final String PUBLIC_NAMESPACE = "public";
}
