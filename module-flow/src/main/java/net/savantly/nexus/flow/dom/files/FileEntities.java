package net.savantly.nexus.flow.dom.files;

import java.io.IOException;
import java.util.List;
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
import org.apache.causeway.applib.value.Blob;
import org.springframework.web.multipart.MultipartFile;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.organizations.dom.organization.Organizations;

@Named(FlowModule.NAMESPACE + ".FileEntities")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Log4j2
public class FileEntities {
    final RepositoryService repositoryService;
    final FileEntityRepository repository;
    final Organizations organizations;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public FileEntity create(
            final Organization organization,
            @Name final String displayName,
            final Blob file) {
        return repositoryService.persist(FileEntity.withName(organization, displayName, file));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<FileEntity> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public Optional<FileEntity> findById(final String id) {
        return repository.findById(id);
    }

    @Programmatic
    public FileEntity uploadFile(String organizationId, MultipartFile file) throws IOException {
        var organization = organizations.getById(organizationId);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found: " + organizationId);
        }
        Blob blob = new Blob(file.getName(), file.getContentType(), file.getBytes());
        var fileEntity = create(organization, file.getOriginalFilename(), blob);
        fileEntity.setFile(blob);
        return fileEntity;
    }

}
