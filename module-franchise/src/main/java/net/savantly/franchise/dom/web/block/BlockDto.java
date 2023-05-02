package net.savantly.franchise.dom.web.block;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;
import net.savantly.franchise.dom.web.blockType.BlockTypeDto;

@Data
@Accessors(chain = true)
public class BlockDto {
    
    private Long id;
    private String name;
    private BlockTypeDto blockType;
    private String locationId;
    private Map<String, Object> content = new HashMap<>();
}
