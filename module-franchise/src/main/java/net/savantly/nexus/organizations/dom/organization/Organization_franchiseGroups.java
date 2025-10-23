package net.savantly.nexus.organizations.dom.organization;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.inject.Inject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.savantly.nexus.franchise.dom.group.FranchiseGroup;
import net.savantly.nexus.franchise.dom.group.FranchiseGroups;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUsers;


@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class Organization_franchiseGroups {

    final Organization organization;

    public static class ActionEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_franchiseGroups>{}

    @Inject
    OrganizationUsers userRepository;
    @Inject @Transient FranchiseGroups franchiseGroups;
    @Inject @Transient RepositoryService repositoryService;

    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "organization")
    private Set<FranchiseGroup> franchisees = new HashSet<>();

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "franchisees", promptStyle = PromptStyle.DIALOG)
    public FranchiseGroup createFranchisee(
    		@ParameterLayout(named = "Franchisee Name") final String name) {
                val franchisee = franchiseGroups.create(organization, name);
            franchisees.add(franchisee);
        return franchisee;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "franchisees", promptStyle = PromptStyle.DIALOG)
    public Organization addFranchisee(
    		@ParameterLayout(named = "Franchisee") final FranchiseGroup group) {
            franchisees.add(group);
        return organization;
    }
    public List<FranchiseGroup> choices0AddFranchisee() {
        return repositoryService.allInstances(FranchiseGroup.class);
    }

    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "franchisees", promptStyle = PromptStyle.DIALOG)
    public Organization removeFranchisee(
            @ParameterLayout(named = "Franchisee") final FranchiseGroup group) {
        franchisees.removeIf(g -> g.getId().equals(group.getId()));
        return organization;
    }
    public Set<FranchiseGroup> choices0RemoveFranchisee() {
        return this.getFranchisees();
    }
}
