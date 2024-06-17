package net.savantly.nexus.flow.dom.form;

import java.util.Set;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmission;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmissionRepository;

@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Form_submissions {

    final Form object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Form_submissions> {
    }

    @Inject
    @Transient
    FormSubmissionRepository repository;

    @CollectionLayout(named = "Submissions", describedAs = "Submissions of this form", sequence = "99")
    public Set<FormSubmission> coll() {
        return repository.findByFormId(object.getId());
    }
}
