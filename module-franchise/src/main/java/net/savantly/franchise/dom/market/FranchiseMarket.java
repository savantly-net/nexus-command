package net.savantly.franchise.dom.market;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.types.Name;

@Named(FranchiseModule.NAMESPACE + ".FranchiseMarket")
@javax.persistence.Entity
@javax.persistence.Table(
	schema=FranchiseModule.SCHEMA,
	name = "franchise_market",
		    uniqueConstraints = {
		            @javax.persistence.UniqueConstraint(name = "franchisemarket__name__UNQ", columnNames = {"name"})
		        }
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "globe")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FranchiseMarket {

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;

    public static FranchiseMarket withRequiredFields(String id, String name) {
        val entity = new FranchiseMarket();
        entity.id = id;
        entity.name = name;
        return entity;
    }
    
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    
    @Name
    @Title
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;
    

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;
    

}
