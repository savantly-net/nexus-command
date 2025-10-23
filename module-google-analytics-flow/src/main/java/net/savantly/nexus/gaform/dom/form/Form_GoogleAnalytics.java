package net.savantly.nexus.gaform.dom.form;

import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import net.savantly.nexus.flow.dom.form.Form;

@Property(editing = Editing.DISABLED)
@PropertyLayout(fieldSetId = "identity", describedAs = "Google Analytics connection", named = "Google Analytics", sequence = "1.7")
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class Form_GoogleAnalytics {

    final Form form;

    public FormGAConnection prop() {
        return repository.findByFormId(form.getId());
    }

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Form_GoogleAnalytics> {
    }

    @Inject
    @Transient
    FormGAConnectionRepository repository;

}
