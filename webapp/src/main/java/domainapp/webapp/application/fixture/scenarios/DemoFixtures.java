package domainapp.webapp.application.fixture.scenarios;

import jakarta.inject.Inject;

import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixturesService;

import net.savantly.nexus.command.web.fixture.block.Block_persona;
import net.savantly.nexus.command.web.fixture.blockType.BlockType_persona;
import net.savantly.nexus.command.web.fixture.page.WebPage_persona;
import net.savantly.nexus.command.web.fixture.pageBlock.PageBlock_persona;
import net.savantly.nexus.command.web.fixture.site.WebSite_persona;
import net.savantly.nexus.command.web.fixture.siteBlock.SiteBlock_persona;
import net.savantly.nexus.organizations.fixture.organization.Organization_persona;

public class DemoFixtures extends FixtureScript {

    @Override
    protected void execute(final ExecutionContext ec) {
        ec.executeChildren(this, moduleWithFixturesService.getTeardownFixture());
        ec.executeChild(this, new Organization_persona.PersistAll());
        ec.executeChild(this, new BlockType_persona.PersistAll());
        ec.executeChild(this, new Block_persona.PersistAll());
        ec.executeChild(this, new WebSite_persona.PersistAll());
        ec.executeChild(this, new SiteBlock_persona.PersistAll());
        ec.executeChild(this, new WebPage_persona.PersistAll());
        ec.executeChild(this, new PageBlock_persona.PersistAll());
    }

    @Inject ModuleWithFixturesService moduleWithFixturesService;

}
