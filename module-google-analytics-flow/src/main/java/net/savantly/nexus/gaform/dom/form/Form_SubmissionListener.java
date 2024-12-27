package net.savantly.nexus.gaform.dom.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.causeway.applib.services.factory.FactoryService;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.api.events.submitFormJson.SubmitFormJsonEvent;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecrets;

@Log4j2
public class Form_SubmissionListener {

    private final String baseUrl = "https://www.google-analytics.com";
    private final String measurementApi = "/mp/collect";

    private final RestClient rest;

    private final FactoryService factoryService;
    private final OrganizationSecrets secrets;

    public Form_SubmissionListener(FactoryService factoryService, OrganizationSecrets secrets) {
        log.info("creating Form_SubmissionListener");
        this.factoryService = factoryService;
        this.secrets = secrets;
        this.rest = RestClient.builder().baseUrl(baseUrl).build();
    }

    @EventListener(classes = SubmitFormJsonEvent.class)
    public void onApplicationEvent(SubmitFormJsonEvent event) {
        log.info("begin handling event: {}", event);
        if (Objects.isNull(event) || Objects.isNull(event.getSource())
                || Objects.isNull(event.getSource().getSubmission())) {
            return;
        }

        var source = event.getSource();
        var submission = source.getSubmission();
        var form = submission.getForm();

        Form_GoogleAnalytics mixin = factoryService.mixin(Form_GoogleAnalytics.class, form);

        if (Objects.isNull(mixin)) {
            log.warn("google analytics mixin is null: {}", form.getId());
            return;
        }

        var gaConnection = mixin.prop();

        if (Objects.isNull(gaConnection) || Objects.isNull(gaConnection.getConnection())) {
            log.debug("google analytics connection not associated with formId: {}", form.getId());
            return;
        }

        var apiKey = gaConnection.getConnection().getApiKey();

        if (Objects.isNull(apiKey) || Objects.isNull(apiKey.getSecret())) {
            log.debug("google analytics api key not found for formId: {}", form.getId());
            return;
        }

        var unencryptedKey = secrets.decryptSecretString(apiKey);
        var uriVariables = Map.of("api_secret", unencryptedKey);

        var eventParams = Map.of("form_id", form.getId(), "submission_id", submission.getId(), "form_name",
                form.getName());
        var gaEvent = Map.of("name", "form_submission", "params", eventParams);
        var events = List.of(gaEvent);
        var body = Map.of("events", events, "user_id", extractUserId(source.getRequest()));

        var response = rest.post()
                .uri(measurementApi, uriVariables)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.isError(), (req, res) -> {
                    log.error("Failed to send event to Google Analytics: {}", res.getStatusText());
                })
                .toBodilessEntity();
        log.info("end handling event with GA response: {}", response);
    }

    private String extractUserId(HttpServletRequest request) {

        var cid = request.getParameter("cid");
        if (Objects.nonNull(cid)) {
            return cid;
        }
        var clientId = request.getParameter("client_id");
        if (Objects.nonNull(clientId)) {
            return clientId;
        }
        var proxiedFor = request.getParameter("X-Forwarded-For");
        if (Objects.nonNull(proxiedFor)) {
            return proxiedFor;
        }
        return request.getRemoteAddr();
    }
}