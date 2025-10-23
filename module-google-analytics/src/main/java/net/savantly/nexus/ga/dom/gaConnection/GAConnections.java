package net.savantly.nexus.ga.dom.gaConnection;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.ga.GoogleAnalyticsModule;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(GoogleAnalyticsModule.NAMESPACE + ".GAConnections")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
@Log4j2
public class GAConnections {

    final ObjectMapper objectMapper;
    final GAConnectionRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public GAConnection create(
            final Organization organization,
            @Name final String name,
            final String measurementId) {
        return repository.save(GAConnection.withRequiredArgs(organization, name, measurementId));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<GAConnection> listAll() {
        return repository.findAll();
    }

}
