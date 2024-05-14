package net.savantly.nexus.orgfees.dom.invoice;

import javax.inject.Named;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.savantly.nexus.organizations.OrganizationsModule;

@Accessors(chain = true)
@DomainObject(nature = Nature.VIEW_MODEL, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@Named(OrganizationsModule.NAMESPACE + ".MonthlyOrgReportItem")
@DomainObjectLayout(cssClassFa = "file-lines", named = "Monthly Organization Report Item")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(onlyExplicitlyIncluded = false)
@XmlType(
        propOrder = {
                "organizationName",
                "productName",
                "productDescription",
                "productBillingInterval",
                "productBillingAmount"
        }
)
@XmlAccessorType(XmlAccessType.FIELD)
public class MonthlyOrgReportItem {

    @XmlTransient
    public String getTitle() {
        return String.format("{} - {}", organizationName, productName);
    }
    
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity")
    @XmlElement(required = true)
    private String organizationName;

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity")
    @XmlElement(required = true)
    private String productName;

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity")
    @XmlElement(required = true)
    private String productDescription;

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity")
    @XmlElement(required = true)
    private String productBillingInterval;

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity")
    @XmlElement(required = true)
    private double productBillingAmount;
}
