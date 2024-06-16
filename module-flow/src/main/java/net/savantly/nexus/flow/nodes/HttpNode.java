package net.savantly.nexus.flow.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeType;
import net.savantly.nexus.flow.dom.flowNode.impl.BasicFlowNode;
import net.savantly.nexus.flow.dom.flowNodeParameter.FlowNodeParameter;

@Log4j2
@FlowNodeType(name = "HTTP", description = "HTTP Request")
@Schema(name = "HttpNode", description = "An HTTP Request node")
public class HttpNode extends BasicFlowNode {

    public HttpNode(String id) {
        super(id);
    }

    @Getter
    @Setter
    @Max(2048)
    @FlowNodeParameter("url")
    private String url;

    @Getter
    @Setter
    @Schema(description = "schema The HTTP method to use", allowableValues = { "GET", "POST", "PUT", "DELETE",
            "PATCH" })
    @FlowNodeParameter("method")
    private String method = "GET";

    @Getter
    @Setter
    @FlowNodeParameter("body")
    private String body;

    @Getter
    @Setter
    @FlowNodeParameter("headers")
    private Collection<String> headers = new ArrayList<>();

    @Getter
    @Setter
    @FlowNodeParameter("responseVariable")
    @Schema(name = "responseVariable", description = "The variable to store the response in")
    private String responseVariable = "response";

    @Override
    public void execute(FlowContext context) {

        var multiValueMapHeaders = new HttpHeaders();
        if (Objects.nonNull(headers) && !headers.isEmpty()) {
            headers.forEach(h -> {
                if (Objects.isNull(h) || h.trim().isBlank())
                    return;
                if (!h.contains(":")) {
                    throw new IllegalArgumentException("Header must be in the format 'key:value'");
                }
                var parts = h.split(":");
                multiValueMapHeaders.add(parts[0], parts[1]);
            });
        }

        var restTemplate = new RestTemplate();
        var request = new HttpEntity<>(body, multiValueMapHeaders);

        log.info("Executing HTTP request to {} with method {}", url, method);

        var response = restTemplate.exchange(url, HttpMethod.valueOf(method), request, String.class);
        context.setVariable(responseVariable, response.getBody());
    }

}
