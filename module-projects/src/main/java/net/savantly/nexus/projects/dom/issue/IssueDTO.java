package net.savantly.nexus.projects.dom.issue;

import lombok.Data;

@Data
public class IssueDTO {
    private String name;
    private String description;
    private String labels;
    private int issueOrder;
}
