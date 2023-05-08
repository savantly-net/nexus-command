package net.savantly.nexus.command.web.dom.block;

import lombok.val;
import net.savantly.nexus.command.web.dom.blockType.BlockType;
import net.savantly.nexus.command.web.dom.blockType.BlockTypeDto;
import net.savantly.nexus.command.web.dom.blockType.BlockTypeDtoConverter;

public class BlockDtoConverter {
    

    public static BlockDto toDto(Block blockType) {
        val dto = new BlockDto();
        dto.setId(blockType.getId());
        dto.setName(blockType.getName());
        dto.setContent(blockType.getContent());
        dto.setBlockType(asDto(blockType.getBlockType()));
        return dto;
    }

    private static BlockTypeDto asDto(BlockType blockType) {
        return BlockTypeDtoConverter.toDto(blockType);
    }
}
