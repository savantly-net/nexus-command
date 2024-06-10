package net.savantly.nexus.flow.javet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.caoccao.javet.enums.JSRuntimeType;
import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.caoccao.javet.interop.engine.JavetEngineConfig;
import com.caoccao.javet.interop.engine.JavetEnginePool;

import lombok.extern.log4j.Log4j2;

@Configuration
@ConfigurationProperties(prefix = "javet.engine")
@Log4j2
public class JavetConfiguration {

    private boolean allowEval = false;
    private boolean autoSendGCNotification = true;
    private int defaultEngineGaurdTimeoutMillis = 30000;
    private int poolDaemonCheckIntervalMillis = JavetEngineConfig.DEFAULT_POOL_DAEMON_CHECK_INTERVAL_MILLIS;
    private int poolIdleTimeoutSeconds = JavetEngineConfig.DEFAULT_POOL_IDLE_TIMEOUT_SECONDS;
    private int poolMinSize = JavetEngineConfig.DEFAULT_POOL_MIN_SIZE;
    private int poolMaxSize = JavetEngineConfig.DEFAULT_POOL_MIN_SIZE;
    private int poolShutdownTimeoutSeconds = JavetEngineConfig.DEFAULT_POOL_SHUTDOWN_TIMEOUT_SECONDS;
    private int resetEngineTimeoutSeconds = JavetEngineConfig.DEFAULT_RESET_ENGINE_TIMEOUT_SECONDS;

    @Bean(name = "JavetEnginePoolNode")
    public IJavetEnginePool getJavetEnginePoolNode() {
        JavetEngineConfig javetEngineConfigNode = new JavetEngineConfig();
        javetEngineConfigNode.setAllowEval(allowEval);
        javetEngineConfigNode.setAutoSendGCNotification(autoSendGCNotification);
        javetEngineConfigNode.setDefaultEngineGuardTimeoutMillis(defaultEngineGaurdTimeoutMillis);
        javetEngineConfigNode.setPoolDaemonCheckIntervalMillis(poolDaemonCheckIntervalMillis);
        javetEngineConfigNode.setPoolIdleTimeoutSeconds(poolIdleTimeoutSeconds);
        javetEngineConfigNode.setPoolMinSize(poolMinSize);
        javetEngineConfigNode.setPoolMaxSize(poolMaxSize);
        javetEngineConfigNode.setPoolShutdownTimeoutSeconds(poolShutdownTimeoutSeconds);
        javetEngineConfigNode.setResetEngineTimeoutSeconds(resetEngineTimeoutSeconds);
        javetEngineConfigNode.setJavetLogger(new JavetLogger());
        javetEngineConfigNode.setJSRuntimeType(JSRuntimeType.Node);
        return new JavetEnginePool<>(javetEngineConfigNode);
    }
}