package net.savantly.nexus.command.web.dom.site;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import net.savantly.nexus.command.web.dom.og.OpenGraphData;
import net.savantly.nexus.command.web.dom.page.WebPageDto;

@Data
@Accessors(chain = true)
public class WebSiteDto {
    
    private String id;
    private String name;
    private String url;
    private OpenGraphData openGraphData;
    private List<WebPageDto> webPages = new ArrayList<>();
}
