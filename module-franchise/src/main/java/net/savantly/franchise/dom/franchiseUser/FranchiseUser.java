package net.savantly.franchise.dom.franchiseUser;

import java.util.Comparator;

import javax.inject.Named;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.mixins.security.HasUsername;

import lombok.Getter;
import lombok.Setter;
import net.savantly.franchise.FranchiseModule;

@Named(FranchiseModule.NAMESPACE + ".FranchiseUser")
@DomainObject(nature = Nature.VIEW_MODEL)
@XmlRootElement(name = "FranchiseUser")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class FranchiseUser implements HasUsername, Comparable<FranchiseUser> {
    
    private String username;

    @Title
    private String displayName;

    // *** IMPLEMENTATIONS ****

    private final static Comparator<FranchiseUser> comparator =
            Comparator.comparing(FranchiseUser::getUsername);

    @Override
    public int compareTo(final FranchiseUser other) {
        return comparator.compare(this, other);
    }
}
