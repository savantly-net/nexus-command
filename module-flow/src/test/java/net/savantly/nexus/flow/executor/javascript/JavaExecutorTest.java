package net.savantly.nexus.flow.executor.javascript;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.javet.JavetConfiguration;

@Log4j2
public class JavaExecutorTest {
    

    final JavetConfiguration javetConfiguration = new JavetConfiguration();

    @Test
    public void testExecute() throws Exception {

        var script = "ctx.setVariable('payload', 'Hello Back');";
        var context = new FlowContext();

        context.setVariable("payload", "Hello World");
        
        var jsExecutor = new JavascriptExecutor(() -> javetConfiguration.getJavetEnginePoolNode());
        var result = jsExecutor.execute(script, context);

        log.info("Execution result: {}", result);

        assertTrue(context.getVariable("payload").equals("Hello Back"));

    }

    @Test
    public void testContextIsolation() throws Exception {

        var jsExecutor = new JavascriptExecutor(() -> javetConfiguration.getJavetEnginePoolNode());

        var test1 = "Hello Back";
        var test2 = "Hello Again";

        var script1 = "ctx.setVariable('payload', '" + test1 +"');";
        var context1 = new FlowContext();
        context1.setVariable("payload", "Hello World");
        var result1 = jsExecutor.execute(script1, context1);
        log.info("Execution result: {}", result1);

        var script2 = "ctx.setVariable('payload', '" + test2 +"');";
        var context2 = new FlowContext();
        context2.setVariable("payload", "Hello World");
        var result2 = jsExecutor.execute(script2, context2);
        log.info("Execution result: {}", result2);

        assertTrue(context1.getVariable("payload").equals(test1));
        assertTrue(context2.getVariable("payload").equals(test2));

    }

    @Test
    public void testContextIsolationWithConstants() throws Exception {

        var jsExecutor = new JavascriptExecutor(() -> javetConfiguration.getJavetEnginePoolNode());

        var test1 = "Hello Back";
        var test2 = "Hello Again";

        var script1 = "const test = '" + test1 + "';";
        var context1 = new FlowContext();
        var result1 = jsExecutor.execute(script1, context1);
        log.info("Execution result: {}", result1);

        var script2 = "const test = '" + test2 + "';";
        var context2 = new FlowContext();
        var result2 = jsExecutor.execute(script2, context2);
        log.info("Execution result: {}", result2);

    }
}
