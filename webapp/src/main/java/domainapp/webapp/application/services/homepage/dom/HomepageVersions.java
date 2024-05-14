package domainapp.webapp.application.services.homepage.dom;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import domainapp.webapp.application.ApplicationModule;

@Named(ApplicationModule.PUBLIC_NAMESPACE + ".HomepageVersions")
@DomainService(
        nature = NatureOfService.VIEW
)
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class HomepageVersions {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final HomepageVersionRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public HomepageVersion create() {
        return repositoryService.persist(HomepageVersion.withRequiredFields());
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<HomepageVersion> listAll() {
        return repository.findAll();
    }
    
}
