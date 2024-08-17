package net.savantly.nexus.flow.dom.emailTarget;

import org.apache.causeway.applib.services.email.EmailService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;

@RequiredArgsConstructor
@Log4j2
public class EmailDestinationHookFactory {

    private final EmailService emailService;
    private final EmailTargets emailTargets;
    private final JavascriptExecutor javascriptExecutor;
    private final FlowContextFactory flowContextFactory;
    private final RepositoryService repositoryService;
    private final ObjectMapper objectMapper;

    public EmailDestinationHook createHook() {
        return new EmailDestinationHook(emailService, emailTargets, javascriptExecutor, flowContextFactory,
                repositoryService, objectMapper);
    }

}
