package net.savantly.nexus.command.web.dom.page;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.experimental.Accessors;
import net.savantly.nexus.command.web.dom.og.OpenGraphData;

@Data
@Accessors(chain = true)
public class WebPageDto {
    
    private String id;
    private String name;
    private OpenGraphData openGraphData;
    private ZonedDateTime publishDate;
}
