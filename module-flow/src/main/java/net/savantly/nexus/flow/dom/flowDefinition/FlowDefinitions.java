package net.savantly.nexus.flow.dom.flowDefinition;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.flow.dto.FlowDto;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".Flows")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Log4j2
public class FlowDefinitions {
    final RepositoryService repositoryService;
    final FlowDefinitionRepository repository;
    final FlowDefinitionExecutionProxy flowDefinitionExecutionProxy;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public FlowDefinition create(
            final Organization organization,
            @Name final String name) {
        return repositoryService.persist(FlowDefinition.withName(organization, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<FlowDefinition> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public Optional<FlowDefinition> findById(final String id) {
        return repository.findById(id);
    }

    @Programmatic 
    public Set<FlowDefinition> findByOrganizationId(final String organizationId) {
        return repository.findByOrganizationId(organizationId);
    }

    @Programmatic
    public FlowDefinition createFromDto(
        final Organization organization, final FlowDto dto) {
        return repositoryService.persist(FlowDefinition.fromDto(organization, dto));
    }

    @Programmatic
    public void executeFlow(final String flowId, final Object payload, final String apiKey) {
        flowDefinitionExecutionProxy.executeFlow(flowId, payload, apiKey);
    }

}
