package net.savantly.nexus.flow.dom.form;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destinations.DestinationHookFactory;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmission;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmissionRepository;

@Log4j2
@RequiredArgsConstructor
public class FormSubmissionProxy {

    final FormSubmissionRepository repository;
    final FormRepository formRepository;
    final ObjectMapper objectMapper;
    final DestinationHookFactory destinationHookFactory;

    public void submitForm(Form form, Map<String, Object> payload, String apiKey) throws JsonProcessingException {
        
        if (!form.isPublicForm()) {
            if (form.getApiKey() == null) {
                throw new IllegalArgumentException("api-key required");
            }
            if (!form.getApiKey().equals(apiKey)) {
                throw new IllegalArgumentException("api-key does not match");
            }
        }
        var stringPayload = objectMapper.writeValueAsString(payload);
        var submission = FormSubmission.withRequiredArgs(form, stringPayload);
        repository.save(submission);
        
        var destinations = form.getDestinations();
        log.info("executing form hooks: " + destinations.size());
        for (var destination : destinations) {
            var destinationHook = destinationHookFactory.createHook(destination);
            var result = destinationHook.execute(destination, payload, form.getMappings());
            log.info("hook executed: " + result);
        }
    }
    
}
