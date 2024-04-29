package net.savantly.nexus.projects.dom.persona;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.experimental.Accessors;

@Embeddable
@Data
@Accessors(chain = true)
class DemographicInfo {
    private int age;
    private String gender;
    private String educationLevel;
    private String occupation;
    private String location;
}