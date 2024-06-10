package net.savantly.nexus.flow.api;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDescriptor;

@RestController
@RequestMapping("${nexus.modules.flow.api-path:/api/flow}")
@RequiredArgsConstructor
public class FlowApi {

    private final FlowService flowService;

    @GetMapping("/definitions")
    public FlowDefinitions getFlowDefinitions() {
        return flowService.getFlowDefinitions();
    }

    @GetMapping("/node-types")
    public Set<FlowNodeDescriptor> getFlowNodeDiscoveryService() {
        return flowService.getAvailableFlowNodeDescriptors();
    }

    @PostMapping("/execute/{flowId}")
    public void executeFlow(@PathVariable String flowId, @RequestBody Object payload, @RequestHeader("api-key") String apiKey){
        flowService.executeFlow(flowId, payload, apiKey);
    }
    
}
