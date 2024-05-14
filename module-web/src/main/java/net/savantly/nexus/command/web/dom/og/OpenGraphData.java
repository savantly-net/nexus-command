package net.savantly.nexus.command.web.dom.og;

import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.savantly.nexus.command.web.NexusCommandWebModule;

@Embeddable
@Getter @Setter
@Accessors(chain = true)
@ToString
@Named(NexusCommandWebModule.NAMESPACE + ".OpenGraphData")
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
