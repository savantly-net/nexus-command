package net.savantly.nexus.projects.dom.persona;

import java.util.List;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.experimental.Accessors;

@Embeddable
@Data
@Accessors(chain = true)
class ProfessionalBackground {
    private String industry;
    private String jobRole;
    private String careerPath;
    private List<String> skills;
}
