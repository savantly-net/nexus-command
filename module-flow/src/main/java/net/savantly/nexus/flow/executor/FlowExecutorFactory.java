package net.savantly.nexus.flow.executor;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dto.FlowDto;

@RequiredArgsConstructor
public class FlowExecutorFactory {

    private final FlowNodeFactory nodeFactory;

    public FlowExecutor createExecutor(String flowJson) {
        try {
            return new FlowExecutor(nodeFactory, flowJson);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create flow executor", e);
        }
    }

    public FlowExecutor createExecutor(FlowDto flow) {
        return new FlowExecutor(nodeFactory, flow);
    }
    
}
