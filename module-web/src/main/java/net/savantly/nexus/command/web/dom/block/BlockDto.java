package net.savantly.nexus.command.web.dom.block;

import lombok.Data;
import lombok.experimental.Accessors;
import net.savantly.nexus.command.web.dom.blockType.BlockTypeDto;

@Data
@Accessors(chain = true)
public class BlockDto {
    
    private String id;
    private String name;
    private BlockTypeDto blockType;
    private String locationId;
    private String content;
}
