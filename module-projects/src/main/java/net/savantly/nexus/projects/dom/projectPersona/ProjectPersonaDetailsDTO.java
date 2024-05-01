package net.savantly.nexus.projects.dom.projectPersona;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectPersonaDetailsDTO {
    
    private String needs;
    private String tasksToAccomplish;
    private String influenceFactors;
    private String trustedInformationSources;
    private String useCases;
}
