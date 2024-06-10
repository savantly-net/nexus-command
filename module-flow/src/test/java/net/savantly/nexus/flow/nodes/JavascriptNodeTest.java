package net.savantly.nexus.flow.nodes;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.caoccao.javet.enums.JSRuntimeType;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.engine.JavetEnginePool;

import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;

public class JavascriptNodeTest {

    @Test
    public void testJavascriptNode() throws JavetException {

        try (JavetEnginePool<NodeRuntime> javetEnginePool = new JavetEnginePool<NodeRuntime>()) {
            javetEnginePool.getConfig().setJSRuntimeType(JSRuntimeType.Node);
            var javascriptExecutor = new JavascriptExecutor(javetEnginePool);

            JavascriptNode node = new JavascriptNode("test", javascriptExecutor);
            node.setScript("console.log('Hello World!')");

            node.execute(FlowContext.empty());
        }

    }

    @Test
    public void testJavascriptNodeWithException() throws JavetException {
        try (JavetEnginePool<NodeRuntime> javetEnginePool = new JavetEnginePool<NodeRuntime>()) {
            javetEnginePool.getConfig().setJSRuntimeType(JSRuntimeType.Node);
            var javascriptExecutor = new JavascriptExecutor(javetEnginePool);

            JavascriptNode node = new JavascriptNode("test", javascriptExecutor);

            node.setScript("throw new Error('Test Error')");

            try {
                node.execute(FlowContext.empty());
            } catch (Exception e) {
                return;
            }
            fail("Expected exception to be thrown");
        }
    }

    @Test
    public void testJavascriptNodeWithVariable() throws JavetException {
        try (JavetEnginePool<NodeRuntime> javetEnginePool = new JavetEnginePool<NodeRuntime>()) {
            javetEnginePool.getConfig().setJSRuntimeType(JSRuntimeType.Node);
            var javascriptExecutor = new JavascriptExecutor(javetEnginePool);

            JavascriptNode node = new JavascriptNode("test", javascriptExecutor);

            node.setScript("ctx.setVariable('test', 'Hello World!')");

            FlowContext context = FlowContext.empty();
            node.execute(context);

            if (!"Hello World!".equals(context.getVariable("test"))) {
                fail("Variable not set correctly");
            }
        }

    }

    @Test
    public void testJavascriptNodeWithFetch() throws JavetException {
        try (JavetEnginePool<NodeRuntime> javetEnginePool = new JavetEnginePool<NodeRuntime>()) {
            javetEnginePool.getConfig().setJSRuntimeType(JSRuntimeType.Node);
            var javascriptExecutor = new JavascriptExecutor(javetEnginePool);

            JavascriptNode node = new JavascriptNode("test", javascriptExecutor);
            node.setScript(
                    "fetch('https://jsonplaceholder.typicode.com/todos/1').then(response => response.json()).then(json => ctx.setVariable('test', json.title))");

            FlowContext context = FlowContext.empty();
            node.execute(context);

            if (!"delectus aut autem".equals(context.getVariable("test"))) {
                fail("Variable not set correctly");
            }
        }

    }
}
