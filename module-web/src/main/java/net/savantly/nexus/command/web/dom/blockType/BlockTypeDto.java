package net.savantly.nexus.command.web.dom.blockType;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BlockTypeDto {
    
    private String id;
    private String name;
    private String description;
    private String schema;
    private String uiSchema;

}
