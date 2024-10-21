package net.savantly.nexus.flow.dom.form;

import java.time.ZonedDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destination.DestinationHookFactory;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmission;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmissions;
import net.savantly.nexus.flow.dom.recaptcha.ReCaptchaService;

@Log4j2
@RequiredArgsConstructor
public class FormSubmissionProxy {

    final FormSubmissions formSubmissions;
    final FormRepository formRepository;
    final ObjectMapper objectMapper;
    final DestinationHookFactory destinationHookFactory;
    final ReCaptchaService reCaptchaService;

    /**
     * Bypasses API key and recaptcha checks
     */
    public void submitForm(Form form, Map<String, Object> payload)
            throws JsonProcessingException {

        var stringPayload = objectMapper.writeValueAsString(payload);
        var submission = formSubmissions.create(form, stringPayload);

        payload.put("_form_id", form.getId());
        payload.put("_form_name", form.getName());
        payload.put("_submission_id", submission.getId());
        payload.put("_submission_datetime", now());

        var destinations = form.getDestinations();
        log.info("executing form hooks: " + destinations.size());
        for (var destination : destinations) {
            try (var destinationHook = destinationHookFactory.createHook(destination)) {
                var result = destinationHook.execute(destination, payload, form.getMappings());
                log.info("hook executed: " + result);
            } catch (Exception e) {
                log.error("hook failed", e);

            }
        }

    }

    public void submitFormForDestination(Form form, Map<String, Object> payload, String destinationId)
            throws JsonProcessingException {

        var stringPayload = objectMapper.writeValueAsString(payload);
        var submission = formSubmissions.create(form, stringPayload);

        payload.put("_form_id", form.getId());
        payload.put("_form_name", form.getName());
        payload.put("_submission_id", submission.getId());
        payload.put("_submission_datetime", now());

        var destinations = form.getDestinations();
        log.info("executing form hooks: " + destinations.size());
        for (var destination : destinations) {
            if (destination.getId().equals(destinationId)) {
                try (var destinationHook = destinationHookFactory.createHook(destination)) {
                    var result = destinationHook.execute(destination, payload, form.getMappings());
                    log.info("hook executed for destination: " + result);
                } catch (Exception e) {
                    log.error("hook failed", e);

                }
            }
        }
    }

    public void triggerDestinationForSubmission(FormSubmission submission, String destinationId) {
        var form = submission.getForm();
        var stringPayload = submission.getPayload();
        Map payload = null;
        try {
            payload = objectMapper.readValue(stringPayload, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to parse payload", e);
        }

        payload.put("_form_id", form.getId());
        payload.put("_form_name", form.getName());
        payload.put("_submission_id", submission.getId());
        payload.put("_submission_datetime", now());

        var destinations = form.getDestinations();
        log.info("executing form hooks: " + destinations.size());
        for (var destination : destinations) {
            if (destination.getId().equals(destinationId)) {
                try (var destinationHook = destinationHookFactory.createHook(destination)) {
                    var result = destinationHook.execute(destination, payload, form.getMappings());
                    log.info("hook executed for destination: " + result);
                } catch (Exception e) {
                    log.error("hook failed", e);

                }
            }
        }
    }

    public void submitForm(Form form, Map<String, Object> payload, String apiKey, String recaptcha, String clientIP)
            throws JsonProcessingException {

        if (!form.isPublicForm()) {
            if (form.getApiKey() == null) {
                throw new IllegalArgumentException("api-key required");
            }
            if (!form.getApiKey().equals(apiKey)) {
                throw new IllegalArgumentException("api-key does not match");
            }
        }

        if (form.isRecaptchaEnabled()) {
            if (recaptcha == null) {
                throw new IllegalArgumentException("recaptcha required");
            }
            reCaptchaService.processResponse(recaptcha, form.getRecaptchaSecret(), form.getRecaptchaThreshold(),
                    clientIP, form.getRecaptchaAction());
        }

        var stringPayload = objectMapper.writeValueAsString(payload);
        var submission = formSubmissions.create(form, stringPayload);

        payload.put("_form_id", form.getId());
        payload.put("_form_name", form.getName());
        payload.put("_submission_id", submission.getId());
        payload.put("_submission_datetime", now());

        var destinations = form.getDestinations();
        log.info("executing form hooks: " + destinations.size());
        for (var destination : destinations) {
            try (var destinationHook = destinationHookFactory.createHook(destination)) {
                var result = destinationHook.execute(destination, payload, form.getMappings());
                log.info("hook executed: " + result);
            } catch (Exception e) {
                log.error("hook failed", e);

            }
        }
    }

    private ZonedDateTime now() {
        return ZonedDateTime.now();
    }

}
