package net.savantly.nexus.audited;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.savantly.nexus.audited.config.AuditedEntityConfig;

@Configuration
@Import(AuditedEntityConfig.class)
public class AuditedEntityModule {

}
