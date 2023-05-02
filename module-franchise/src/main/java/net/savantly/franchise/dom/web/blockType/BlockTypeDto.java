package net.savantly.franchise.dom.web.blockType;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BlockTypeDto {
    
    private Long id;
    private String name;
    private String description;
    private Map<String, Object> schema = new HashMap<>();
    private Map<String, Object> uiSchema = new HashMap<>();

}
