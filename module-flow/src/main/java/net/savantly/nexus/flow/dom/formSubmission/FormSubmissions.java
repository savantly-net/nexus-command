package net.savantly.nexus.flow.dom.formSubmission;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.flow.dom.form.Form;

@Named(FlowModule.NAMESPACE + ".FormSubmissions")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Log4j2
public class FormSubmissions {
    final RepositoryService repositoryService;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(hidden = Where.EVERYWHERE)
    public FormSubmission create(
            final Form form, final String payload) {
        return repositoryService.persist(FormSubmission.withRequiredArgs(form, payload));
    }

}
