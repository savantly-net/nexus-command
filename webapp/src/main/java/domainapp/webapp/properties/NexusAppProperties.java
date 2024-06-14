package domainapp.webapp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "nexus")
public class NexusAppProperties {

    private Seed seed = new Seed();


    private Encryption encryption = new Encryption();

    @Data
    public static class Seed {
        private boolean enabled = true;
        private SeedUser admin = new SeedUser("admin", "admin");
        private SeedUser user = new SeedUser("user", "user");
    }

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

    @Data
    public static class Encryption {
        private String secret = "";
    }

}
