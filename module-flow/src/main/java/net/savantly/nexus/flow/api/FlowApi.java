package net.savantly.nexus.flow.api;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final ObjectMapper mapper;

    @GetMapping("/definitions")
    public FlowDefinitions getFlowDefinitions() {
        return flowService.getFlowDefinitions();
    }

    @GetMapping("/node-types")
    public Set<FlowNodeDescriptor> getFlowNodeDiscoveryService() {
        return flowService.getAvailableFlowNodeDescriptors();
    }

    @PostMapping("/execute/{flowId}")
    public void executeFlow(@PathVariable String flowId, @RequestBody Object payload,
            @RequestHeader(name = "api-key", required = false) String apiKey) {
        log.info("Requested to execute flow: {}", flowId);
        flowService.executeFlow(flowId, payload, apiKey);
    }

    @PostMapping(value = "/submit/{formId}", consumes = "application/json")
    public ResponseEntity submitFormJson(@PathVariable String formId, @RequestBody Map<String, Object> payload,
            @RequestHeader(name = "api-key", required = false) String apiKey,
            @RequestParam(name = "g-recaptcha-response", required = false) String recaptcha,
            @RequestHeader(name = "X-Forwarded-For", required = false) String clientIP)
            throws JsonProcessingException {
        log.info("Requested to submit form: {}", formId);

        if (Objects.isNull(recaptcha)) {
            log.debug("attempting to get recaptcha from payload");
            if (payload.containsKey("g-recaptcha-response")) {
                var rcFromPayload = payload.get("g-recaptcha-response");
                if (rcFromPayload instanceof String) {
                    recaptcha = (String) rcFromPayload;
                } else {
                    recaptcha = mapper.writeValueAsString(rcFromPayload);
                }
            } else {
                log.debug("recaptcha not found in payload");
            }
        }

        try {
            flowService.submitForm(formId, payload, apiKey, recaptcha, clientIP);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            var errBody = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(errBody);
        }
    }

}
