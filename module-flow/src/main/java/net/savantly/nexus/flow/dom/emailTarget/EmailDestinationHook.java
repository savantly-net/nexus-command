package net.savantly.nexus.flow.dom.emailTarget;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.causeway.applib.services.email.EmailService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destination.AbstractBaseDestinationHook;
import net.savantly.nexus.flow.dom.destination.Destination;
import net.savantly.nexus.flow.dom.destination.DestinationHookResponse;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.dom.flowContext.VariableReplacement;
import net.savantly.nexus.flow.dom.formMapping.Mapping;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;

@Log4j2
public class EmailDestinationHook extends AbstractBaseDestinationHook {

    private final EmailService emailService;
    private final EmailTargets emailTargets;
    private final FlowContextFactory flowContextFactory;

    public EmailDestinationHook(EmailService emailService, EmailTargets emailTargets,
            JavascriptExecutor javascriptExecutor, FlowContextFactory flowContextFactory,
            RepositoryService repositoryService, ObjectMapper objectMapper) {
        super(flowContextFactory, javascriptExecutor, repositoryService, objectMapper);
        this.emailService = emailService;
        this.emailTargets = emailTargets;
        this.flowContextFactory = flowContextFactory;
    }

    @Override
    public void close() throws Exception {
        // nothing to do
    }

    @Override
    public DestinationHookResponse sendData(Destination destination, Map<String, Object> payload,
            Collection<? extends Mapping> formMappings) {

        var context = flowContextFactory.create(destination.getOrganization().getId());
        context.setVariable("payload", payload);

        log.info("Sending email to {}", destination.getName());

        var emailTarget = emailTargets.findById(destination.getDestinationId()).orElseThrow();

        var to = emailTarget.getTo();
        var cc = emailTarget.getCc();
        var bcc = emailTarget.getBcc();
        var subject = emailTarget.getSubject();
        var body = emailTarget.getBody();

        try {
            emailService.send(orEmptyList(VariableReplacement.replaceVariables(to, context)),
                    orEmptyList(VariableReplacement.replaceVariables(cc, context)),
                    orEmptyList(VariableReplacement.replaceVariables(bcc, context)),
                    VariableReplacement.replaceVariables(subject, context),
                    VariableReplacement.replaceVariables(body, context));
            return DestinationHookResponse.success();
        } catch (Exception e) {
            log.error("Failed to send email", e);
            return DestinationHookResponse.failure("Failed to send email. " + e.getMessage());
        }
    }

    private List<String> orEmptyList(String value) {
        if (value == null) {
            return List.of();
        }
        return List.of(value);
    }

}
