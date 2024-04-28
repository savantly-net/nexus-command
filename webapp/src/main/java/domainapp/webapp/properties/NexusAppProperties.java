package domainapp.webapp.properties;

import org.springframework.context.annotation.Configuration;
import org.apache.causeway.core.config.CausewayConfiguration.Extensions.Secman.Seed;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "nexus")
public class NexusAppProperties {

    private SeedUser adminSeed = new SeedUser("admin", "admin");
    private SeedUser userSeed = new SeedUser("user", "user");

    @Data
    public static class SeedUser {
        private boolean enabled = true;
        private String username;
        private String password;
        public SeedUser(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
    
}
