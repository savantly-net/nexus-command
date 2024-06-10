package net.savantly.nexus.flow.executor.javascript;

import java.util.HashMap;
import java.util.Objects;

import com.caoccao.javet.enums.V8AwaitMode;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.caoccao.javet.values.reference.V8ValuePromise;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;

@Log4j2
@RequiredArgsConstructor
public class JavascriptExecutor {

    final IJavetEnginePool javetEnginePool;

    public Object execute(String script, FlowContext context) throws Exception {
        var ctx = new HashMap<String, Object>();
        ctx.put("ctx", context);

        try (var engine = javetEnginePool.getEngine()) {

            try (var runtime = engine.getV8Runtime()) {

                if (Objects.isNull(script) || script.isBlank()) {
                    log.info("No script to execute");
                    return null;
                }

                // Use JavetProxyConverter to convert the Java object
                JavetProxyConverter javetProxyConverter = new JavetProxyConverter();
                var v8ValueFunction = javetProxyConverter.toV8Value(runtime, context);

                log.info("Executing script: {}", script);
                var contextSet = runtime.getGlobalObject().set("ctx", v8ValueFunction);
                log.info("Context set: {}", contextSet);

                var sanityCheck = runtime.getExecutor("ctx;").executeObject();
                log.debug("Sanity check: {}", sanityCheck);

                var execResult = runtime.getExecutor(script).execute();

                // if the result is a promise, then we need to wait for it to resolve
                if (V8ValuePromise.class.isAssignableFrom(execResult.getClass())) {
                    log.info("Promise detected, waiting for resolution");
                    runtime.await(V8AwaitMode.RunTillNoMoreTasks);
                    execResult = ((V8ValuePromise) execResult).getResult();
                }

                log.info("Execution result: {}", execResult);
                return execResult;
            } catch (Exception e) {
                log.error("Error executing script", e);
                throw e;
            }
        } catch (Exception e) {
            log.error("Error executing script", e);
            throw e;
        }
    }

}
