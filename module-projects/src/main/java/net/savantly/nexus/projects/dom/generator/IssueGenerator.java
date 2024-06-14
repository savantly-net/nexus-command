package net.savantly.nexus.projects.dom.generator;

import java.util.Collections;
import java.util.List;

import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import lombok.Data;
import net.savantly.nexus.projects.dom.issue.IssueDTO;

public interface IssueGenerator {

    @SystemMessage("""
            Generate a list of issues based on the provided context.
            It should be in the format - 
            ```
            {
                "issues": [
                    {
                        "name": "Task 1",
                        "description": "Task 1 description",
                        "labels": "Task 1 labels",
                        "issueOrder": 1
                    },
                    {
                        "name": "Task 2",
                        "description": "Task 2 description",
                        "labels": "Task 2 labels",
                        "issueOrder": 2
                    }
                ]
            }
            ```
            only respond with the JSON object
            """)
    @UserMessage("{{projectContext}}")
    default ListOfIssueDTOs generateIssues(@V("projectContext") String projectContext) {
        return new ListOfIssueDTOs();
    }

    @Data
    public static class ListOfIssueDTOs {
        @Description("The list of issues to be generated")
        private List<IssueDTO> issues = Collections.emptyList();
    }

}
