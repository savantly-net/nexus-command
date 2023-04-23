package domainapp.webapp.application.services.homepage;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.HomePage;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.ObjectSupport;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.value.Markup;
import org.apache.causeway.valuetypes.markdown.applib.value.Markdown;

import domainapp.webapp.application.ApplicationModule;
import domainapp.webapp.application.services.homepage.dom.HomepageVersion;
import domainapp.webapp.application.services.homepage.dom.HomepageVersionRepository;


@Named(ApplicationModule.PUBLIC_NAMESPACE + ".HomePageViewModel")
@DomainObject(nature = Nature.VIEW_MODEL)
@HomePage
@DomainObjectLayout()
public class HomePageViewModel {

    @ObjectSupport public String title() {
        return getLatestPublishedVersion().map(v -> v.getName()).orElse("Homepage");
    }

    @Property
    @PropertyLayout(named = "Body")
    public Markup getBody() {
        return getLatestPublishedVersion().map(v -> createMarkup(v.getBodyAsMarkdown())).orElse(defaultMarkup());
    }

    @Programmatic
    private Markup defaultMarkup() {
        return createMarkup(Markdown.valueOf("## Welcome to the homepage"));
    }

    private Markup createMarkup(Markdown body) {
        return Markup.valueOf(body.asHtml());
    }

    @Programmatic
    private Optional<HomepageVersion> getLatestPublishedVersion() {
        var version = repository.findByPublished(true);
        if (version.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(version.get(0));
    }

    @Inject HomepageVersionRepository repository;
}
