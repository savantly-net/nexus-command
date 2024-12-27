package net.savantly.nexus.flow.api.events.submitFormJson;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmission;

@Data
@Builder
public class SubmitFormJsonEventData {

    private final FormSubmission submission;
    private final String clientIP;
    private final HttpServletRequest request;
}
