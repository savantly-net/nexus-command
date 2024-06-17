package net.savantly.nexus.flow.dom.form;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmission;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmissionRepository;

@RequiredArgsConstructor
public class FormSubmissionProxy {

    final FormSubmissionRepository repository;
    final FormRepository formRepository;
    final ObjectMapper objectMapper;

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
    }
    
}
