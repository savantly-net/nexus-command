package net.savantly.nexus.flow.api;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDescriptor;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDiscoveryService;
import net.savantly.nexus.flow.dom.form.Forms;

@RequiredArgsConstructor
public class FlowService {

    private final FlowDefinitions flowDefinitions;
    private final FlowNodeDiscoveryService flowNodeDiscoveryService;
    private final Forms forms;

    public FlowDefinitions getFlowDefinitions() {
        return flowDefinitions;
    }

    public Set<FlowNodeDescriptor> getAvailableFlowNodeDescriptors() {
        return flowNodeDiscoveryService.discoverFlowNodes();
    }

    public void executeFlow(String flowId, Object payload, String apiKey) {
        flowDefinitions.executeFlow(flowId, payload, apiKey);
    }

    public void submitForm(String formId, Map<String, Object> payload, String apiKey, String recaptcha, String clientIP)
            throws JsonProcessingException {
        forms.submitForm(formId, payload, apiKey, recaptcha, clientIP);
    }

}
