package net.savantly.nexus.flow.executor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNode;
import net.savantly.nexus.flow.dto.FlowDto;
import net.savantly.nexus.flow.dto.FlowEdgeDto;
import net.savantly.nexus.flow.dto.FlowNodeDto;

public class FlowExecutor {

    private final FlowDto flow;
    private final Map<String, FlowNode> nodeMap = new HashMap<>();
    private final Map<String, List<FlowNode>> adjacencyList = new HashMap<>();
    private final Set<String> visitedNodes = new HashSet<>();

    private String startNodeId;

    private final FlowNodeFactory nodeFactory;

    public FlowExecutor(FlowNodeFactory nodeFactory, String flowJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.flow = objectMapper.readValue(flowJson, FlowDto.class);
        this.nodeFactory = nodeFactory;
        initialize();
    }

    public FlowExecutor(FlowNodeFactory nodeFactory, FlowDto flow) {
        this.flow = flow;
        this.nodeFactory = nodeFactory;
        initialize();
    }

    private void initialize() {
        // Create node map and adjacency list
        for (FlowNodeDto nodeDto : flow.getNodes()) {
            FlowNode node = nodeFactory.createNode(nodeDto);
            if (nodeDto.getType().equals("start")) {
                startNodeId = node.getId();
            }
            nodeMap.put(node.getId(), node);
            adjacencyList.put(node.getId(), new ArrayList<>());
        }

        // Create adjacency list
        for (FlowEdgeDto edge : flow.getEdges()) {
            adjacencyList.get(edge.getSource()).add(nodeMap.get(edge.getTarget()));
        }
    }

    public void execute(FlowContext context) {
        FlowNode startNode = nodeMap.get(startNodeId);
        if (startNode != null) {
            executeNode(startNode, context);
        } else {
            throw new IllegalStateException("No start node found in the flow.");
        }
    }

    public void execute() {
        FlowContext context = new FlowContext();
        execute(context);
    }

    private void executeNode(FlowNode node, FlowContext context) {
        if (visitedNodes.contains(node.getId())) {
            return;
        }
        visitedNodes.add(node.getId());

        node.execute(context);

        for (FlowNode neighbor : adjacencyList.get(node.getId())) {
            executeNode(neighbor, context);
        }
    }

}
