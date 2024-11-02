package net.savantly.nexus.flow.dom.formSubmission;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.audited.api.AuditedEntity;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.flow.dom.form.Form;
import net.savantly.nexus.organizations.api.HasOrganization;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".FormSubmission")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = FlowModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FormSubmission extends AuditedEntity implements Comparable<FormSubmission>, HasOrganization {

    public static FormSubmission withRequiredArgs(Form form, String payload) {
        val entity = new FormSubmission();
        entity.id = UUID.randomUUID().toString();
        entity.form = form;
        entity.payload = payload;
        return entity;
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    @Title(prepend = "Form Submission: ")
    private String id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter
    @Setter
    private long version;

    @JoinColumn(name = "form_id", nullable = false)
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "1.1", hidden = Where.PARENTED_TABLES)
    @Getter
    @Setter
    private Form form;

    @Column(columnDefinition = "text", nullable = true)
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "1.5", multiLine = 10)
    @Getter
    @Setter
    private String payload;

    @Override
    @PropertyLayout(fieldSetId = "metadata", sequence = "10.3", hidden = Where.NOWHERE)
    public ZonedDateTime getCreatedDate() {
        return super.getCreatedDate();
    }

    // *** IMPLEMENTATIONS ****

    private final static Comparator<FormSubmission> comparator = Comparator.comparing(FormSubmission::getId);

    @Override
    public int compareTo(final FormSubmission other) {
        return comparator.compare(this, other);
    }

    @Transient
    @Override
    @Programmatic
    public Organization getOrganization() {
        if (form != null) {
            return form.getOrganization();
        }
        return null;
    }

    // *** ACTIONS ***

}
