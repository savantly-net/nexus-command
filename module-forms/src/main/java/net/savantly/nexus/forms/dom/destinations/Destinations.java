package net.savantly.nexus.forms.dom.destinations;

import java.util.List;
import java.util.Optional;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import net.savantly.nexus.common.types.KeyValueDto;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.forms.FormsModule;
import net.savantly.nexus.forms.dom.connections.jdbcConnection.JdbcConnectionRepository;
import net.savantly.nexus.forms.dom.connections.webhook.WebhookRepository;
import net.savantly.nexus.forms.dom.form.Form;

@Named(FormsModule.NAMESPACE + ".Destinations")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Destinations {
    final RepositoryService repositoryService;
    final DestinationRepository repository;

    final WebhookRepository webhookRepository;
    final JdbcConnectionRepository jdbcConnectionRepository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Destination create(
            final Form form,
            final KeyValueDto destination,
            final String name,
            final String collectionName) {

        String destinationId = extractDestinationId(destination);
        DestinationType destinationType = extractDestinationType(destination);
        return repositoryService.persist(Destination.withName(form, destinationType, destinationId, name, collectionName));
    }

    @MemberSupport
    public List<KeyValueDto> choices1Create(Form form) {

        var org = form.getOrganization();

        var allJdbcConnections = webhookRepository.findByOrganizationId(org.getId()).stream()
                .map(d -> new KeyValueDto(d.getName(), formatDestinationValue(DestinationType.JDBC, d.getId())))
                .toList();
        
        var allWebhooks = jdbcConnectionRepository.findByOrganizationId(org.getId()).stream()
                .map(d -> new KeyValueDto(d.getName(), formatDestinationValue(DestinationType.WEBHOOK, d.getId())))
                .toList();

        var allInstances = List.of(allJdbcConnections, allWebhooks).stream()
                .flatMap(List::stream)
                .toList();
        return allInstances;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<Destination> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public Optional<Destination> findById(final String id) {
        return repository.findById(id);
    }

    private String formatDestinationValue(DestinationType destinationType, String destinationId) {
        return destinationType.name() + "|" + destinationId;
    }

    private DestinationType extractDestinationType(KeyValueDto destination) {
        var parts = destination.getKey().split("\\|");
        return DestinationType.valueOf(parts[0]);
    }

    private String extractDestinationId(KeyValueDto destination) {
        var parts = destination.getKey().split("\\|");
        return parts[1];
    }
}
