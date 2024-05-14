package net.savantly.nexus.products;

import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.causeway.testing.fixtures.applib.teardown.jpa.TeardownFixtureJpaAbstract;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import net.savantly.nexus.products.config.ProductsConfig;

@Configuration
@Import({
        ProductsConfig.class
})
@ComponentScan
@EnableJpaRepositories
@EntityScan(basePackageClasses = { ProductsModule.class })
public class ProductsModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "nexus.products";
    public static final String SCHEMA = "products";

    @Override
    public FixtureScript getTeardownFixture() {
        return new TeardownFixtureJpaAbstract() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                // deleteFrom(FranchiseLocation.class);
            }
        };
    }
}