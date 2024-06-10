package net.savantly.nexus.flow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowEdgeDto {

    private String id;
    private String source;
    private String target;
    private String type;
    private String sourceHandle;
    private String targetHandle;
    private boolean animated;

}
