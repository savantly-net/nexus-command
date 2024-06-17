package net.savantly.nexus.flow.dom.form;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.flow.dom.destinations.DestinationHookFactory;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".Forms")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Log4j2
public class Forms {
    final RepositoryService repositoryService;
    final FormRepository repository;
    final DestinationHookFactory destinationHookFactory;
    final ObjectMapper objectMapper;
    final FormSubmissionProxy formSubmissionProxy;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Form create(
            final Organization organization,
            @Name final String name) {
        return repositoryService.persist(Form.withName(organization, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<Form> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public Optional<Form> findById(final String id) {
        return repository.findById(id);
    }

    @Programmatic
    public void executeHook(final Form form, final Map<String, Object> payload) {
        for (var destination : form.getDestinations()) {
            var destinationHook = destinationHookFactory.createHook(destination);
            var result = destinationHook.execute(destination, payload, form.getMappings());
            log.info("hook executed: " + result);
        }
    }

    @Programmatic
    public void submitForm(final String formId, final Map<String, Object> payload, final String apiKey)
            throws JsonProcessingException {
        var form = repository.findById(formId).orElseThrow();
        log.info("submitting form: " + form.getName());
        formSubmissionProxy.submitForm(form, payload, apiKey);

        log.info("executing hooks for form: " + form.getName());
        executeHook(form, payload);

    }

}
