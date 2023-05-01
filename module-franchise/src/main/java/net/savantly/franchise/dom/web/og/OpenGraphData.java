package net.savantly.franchise.dom.web.og;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
@DomainObject
public class OpenGraphData {
    
    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.4")
    @Column(name = "og_title", nullable = true)
    private String title;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.4")
    @Column(name = "og_description", nullable = true)
    private String description;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.4")
    @Column(name = "og_keywords", nullable = true)
    private String keywords;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.4")
    @Column(name = "og_image", nullable = true)
    private String image;

}
