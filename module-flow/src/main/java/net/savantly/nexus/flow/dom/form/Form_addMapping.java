package net.savantly.nexus.flow.dom.form;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import net.savantly.nexus.flow.dom.formMapping.FormMapping;


@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "mappings", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Form_addMapping {

    final Form form;

    public static class ActionEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Form_addMapping>{}

    @Inject
    RepositoryService repositoryService;

    @MemberSupport
    public Form act(
            @ParameterLayout(describedAs = "The json path to the property") final String sourcePath,
            @ParameterLayout(describedAs = "The target key/column name for the destinatation") final String targetPath) {

        return repositoryService.persist(form.addMapping(FormMapping.withName(form, sourcePath, targetPath)));
    }
}
