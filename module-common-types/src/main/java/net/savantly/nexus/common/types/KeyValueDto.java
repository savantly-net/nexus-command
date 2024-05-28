package net.savantly.nexus.common.types;

import java.io.Serializable;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.Title;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@DomainObject(nature = Nature.VIEW_MODEL)
@XmlRootElement(name = "keyValueViewModel")
@XmlType(propOrder = {
        "key",
        "value"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class KeyValueDto implements Serializable {

    public KeyValueDto() {
    }
    
    public KeyValueDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Getter
    @Setter
    @Title
    private String key;

    @Getter
    @Setter
    private String value;
}
