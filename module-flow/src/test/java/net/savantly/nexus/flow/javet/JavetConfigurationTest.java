package net.savantly.nexus.flow.javet;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.caoccao.javet.interop.engine.IJavetEnginePool;

import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;

@Log4j2
public class JavetConfigurationTest {

    private JavetConfiguration javetConfiguration = new JavetConfiguration();
    private IJavetEnginePool javetEnginePool = javetConfiguration.getJavetEnginePoolNode();

    @Test
    public void testExecute() throws JavetException {

        var script = "ctx.setVariable('payload', 'Hello Back');";

        var ctx = new HashMap<String, Object>();
        var context = new FlowContext();

        context.setVariable("payload", "Hello World");
        ctx.put("ctx", context);

        try (var engine = javetEnginePool.getEngine()) {
            try (var runtime = engine.getV8Runtime()) {
                // Use JavetProxyConverter to convert the Java object
                JavetProxyConverter javetProxyConverter = new JavetProxyConverter();
                var v8ValueFunction = javetProxyConverter.toV8Value(runtime, context);

                log.info("Executing script: {}", script);
                var contextSet = runtime.getGlobalObject().set("ctx", v8ValueFunction);
                log.info("Context set: {}", contextSet);

                var sanityCheck = runtime.getExecutor("ctx;").executeObject();
                log.debug("Sanity check: {}", sanityCheck);

                var execResult = runtime.getExecutor(script).execute();
                log.info("Execution result: {}", execResult);
            }
        }
    }
}
