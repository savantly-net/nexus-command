package net.savantly.nexus.command.web.dom.blockType;

public class BlockTypeDtoConverter {
    
    public static BlockTypeDto toDto(BlockType blockType) {
        return new BlockTypeDto()
            .setDescription(blockType.getDescription())
            .setId(blockType.getId())
            .setName(blockType.getName())
            .setSchema(blockType.getSchema())
            .setUiSchema(blockType.getUiSchema());
    }
}
