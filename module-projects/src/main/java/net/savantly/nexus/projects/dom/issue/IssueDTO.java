package net.savantly.nexus.projects.dom.issue;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Data
public class IssueDTO {
    @Description("The name of the issue")
    private String name;
    @Description("The description of the issue")
    private String description;
    @Description("The labels of the issue")
    private String labels;
    @Description("The order of the issue")
    private int issueOrder;
}
