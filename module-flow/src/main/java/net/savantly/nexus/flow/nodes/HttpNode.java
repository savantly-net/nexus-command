package net.savantly.nexus.flow.nodes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeType;
import net.savantly.nexus.flow.dom.flowNode.Parameter;
import net.savantly.nexus.flow.dom.flowNode.impl.BasicFlowNode;

@FlowNodeType(name = "HTTP", description = "HTTP Request")
public class HttpNode extends BasicFlowNode {

    public HttpNode(String id) {
        super(id);
    }

    @Parameter("url")
    @Getter
    @Setter
    private String url;

    @Parameter("method")
    @Getter
    @Setter
    private String method = "GET";

    @Parameter("body")
    @Getter
    @Setter
    private String body;

    @Parameter("headers")
    @Getter
    @Setter
    private Set<String> headers;

    @Parameter(name = "responseVariable", description = "The variable to store the response in")
    @Getter
    @Setter
    private String responseVariable = "response";

    @Override
    public void execute(FlowContext context) {

        var _headers = headers.stream().map(h -> h.split(":")).collect(Collectors.toMap(h -> h[0], h -> List.of(h[1])));
        var multiValueMapHeaders = new HttpHeaders();
        _headers.forEach((k, v) -> multiValueMapHeaders.put(k, v));

        var restTemplate = new RestTemplate();
        var request = new HttpEntity<>(body, multiValueMapHeaders);

        var response = restTemplate.exchange(url, HttpMethod.valueOf(method), request, String.class);
        context.setVariable(responseVariable, response.getBody());
    }

}
