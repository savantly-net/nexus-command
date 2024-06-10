package net.savantly.nexus.flow.executor;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.engine.JavetEnginePool;

import net.savantly.nexus.flow.dto.FlowDto;
import net.savantly.nexus.flow.dto.FlowEdgeDto;
import net.savantly.nexus.flow.dto.FlowNodeDto;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;

public class FlowExecutorTest {

    @Test
    public void testJson() throws IOException {
        String jsonPayload = "{\n" +
                "  \"nodes\": [\n" +
                "    {\n" +
                "      \"id\": \"start\",\n" +
                "      \"type\": \"start\",\n" +
                "      \"position\": { \"x\": 100, \"y\": 100 },\n" +
                "      \"data\": {\n" +
                "        \"label\": \"Start Node\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"2\",\n" +
                "      \"type\": \"logging\",\n" +
                "      \"position\": { \"x\": 200, \"y\": 200 },\n" +
                "      \"data\": {\n" +
                "        \"label\": \"Logging Node\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"edges\": [\n" +
                "    {\n" +
                "      \"id\": \"e1-2\",\n" +
                "      \"source\": \"start\",\n" +
                "      \"target\": \"2\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        var javetEnginePool = new JavetEnginePool<NodeRuntime>();
        JavascriptExecutor javascriptExecutor = new JavascriptExecutor(javetEnginePool);
        FlowNodeFactory flowNodeFactory = new FlowNodeFactory(javascriptExecutor);

        FlowExecutor executor = new FlowExecutor(flowNodeFactory, jsonPayload);
        executor.execute();
    }

    @Test
    public void testFlowDto() throws IOException {
        FlowDto flow = new FlowDto();

        flow
            .addNode(
                new FlowNodeDto().setId("start").setType("start")
            )
            .addNode(
                new FlowNodeDto().setId("2").setType("logging")
            )
            .addNode(
                new FlowNodeDto().setId("3").setType("javascript").addData("script", "console.log('Hello World!')")
            )
            .addEdge(
                new FlowEdgeDto().setId("estart-2").setSource("start").setTarget("2")
            )
            .addEdge(
                new FlowEdgeDto().setId("e2-3").setSource("2").setTarget("3")
            );

        var javetEnginePool = new JavetEnginePool<NodeRuntime>();
        JavascriptExecutor javascriptExecutor = new JavascriptExecutor(javetEnginePool);
        FlowNodeFactory flowNodeFactory = new FlowNodeFactory(javascriptExecutor);

        FlowExecutor executor = new FlowExecutor(flowNodeFactory, flow);
        executor.execute();
    }

}
