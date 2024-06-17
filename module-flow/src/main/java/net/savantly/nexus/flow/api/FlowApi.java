package net.savantly.nexus.flow.api;

import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDescriptor;

@RestController
@RequestMapping("${nexus.modules.flow.api-path:/api/public/flow}")
@RequiredArgsConstructor
@Log4j2
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
    public void executeFlow(@PathVariable String flowId, @RequestBody Object payload, @RequestHeader(name = "api-key", required = false) String apiKey){
        log.info("Requested to execute flow: {}", flowId);
        flowService.executeFlow(flowId, payload, apiKey);
    }

    @PostMapping("/submit/{formId}")
    public ResponseEntity submitForm(@PathVariable String formId, @RequestBody Map<String, Object> payload, @RequestHeader(name = "api-key", required = false) String
            apiKey) throws JsonProcessingException{
        log.info("Requested to submit form: {}", formId);
        try {
            flowService.submitForm(formId, payload, apiKey);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            var errBody = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(errBody);
        }
    }
    
}
