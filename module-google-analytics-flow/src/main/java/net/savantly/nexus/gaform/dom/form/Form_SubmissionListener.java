package net.savantly.nexus.gaform.dom.form;

import java.util.HashMap;
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
    private final String measurementApi = "/mp/collect?api_secret={api_secret}&measurement_id={measurement_id}";

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

        var baseGaConnection = gaConnection.getConnection();
        var apiKey = baseGaConnection.getApiKey();

        if (Objects.isNull(apiKey) || Objects.isNull(apiKey.getSecret())) {
            log.debug("google analytics api key not found for formId: {}", form.getId());
            return;
        }

        var gaDebugMode = baseGaConnection.isDebug() ? "1" : "0";

        var unencryptedKey = secrets.decryptSecretString(apiKey);
        var uriVariables = Map.of("api_secret", unencryptedKey, "measurement_id", baseGaConnection.getMeasurementId());

        var eventParams = new HashMap<String, Object>();
        eventParams.put("form_id", form.getId());
        eventParams.put("submission_id", submission.getId());
        eventParams.put("form_name", form.getName());
        eventParams.put("debug_mode", gaDebugMode);

        // https://developers.google.com/analytics/devguides/collection/protocol/ga4/changelog#2022-05-23
        var sid = extractSessionId(source.getRequest());
        if (Objects.nonNull(sid)) {
            eventParams.put("session_id", sid);
        }

        var eventName = gaConnection.getEventName() != null ? gaConnection.getEventName() : "form_submission";
        var gaEvent = Map.of("name", eventName, "params", eventParams);
        var events = List.of(gaEvent);
        var body = new HashMap<String, Object>();
        body.put("events", events);
        var uid = extractUserId(source.getRequest());
        if (Objects.nonNull(uid)) {
            body.put("user_id", uid);
        }
        var cid = extractClientId(source.getRequest());
        body.put("client_id", cid);

        var fullMeasurementApi = measurementApi;

        if (baseGaConnection.isDebug()) {
            fullMeasurementApi = "/debug" + measurementApi;
        }

        var response = rest.post()
                .uri(fullMeasurementApi, uriVariables)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.isError(), (req, res) -> {
                    log.error("Failed to send event to Google Analytics: {}", res.getStatusText());
                })
                .toEntity(Object.class);
        log.info("end handling event with GA response: {}", response);
    }

    private String extractUserId(HttpServletRequest request) {

        var uid = request.getHeader("x-ga4-user-id");
        if (Objects.nonNull(uid)) {
            return uid;
        }
        return null;
    }

    private String extractClientId(HttpServletRequest request) {

        var cid = request.getHeader("x-ga4-client-id");
        if (Objects.nonNull(cid)) {
            return cid;
        }
        return request.getSession().getId();
    }

    private String extractSessionId(HttpServletRequest request) {
        var sid = request.getHeader("x-ga4-session-id");
        if (Objects.nonNull(sid)) {
            return sid;
        }
        return request.getSession().getId();
    }
}
