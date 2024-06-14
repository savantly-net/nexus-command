package net.savantly.nexus.projects.dom.projectPersona;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectPersonaDetailsDTO {
    
    @Description("Factors that influence the persona's decision making")
    private String influenceFactors;

    @Description("The persona's trusted information sources")
    private String trustedInformationSources;
}
