package net.savantly.nexus.gaform.dom.form;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.causeway.applib.services.eventbus.EventBusService;
import org.apache.causeway.applib.services.factory.FactoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import jakarta.servlet.http.HttpServletRequest;
import net.savantly.encryption.jpa.AttributeEncryptor;
import net.savantly.nexus.flow.api.events.submitFormJson.SubmitFormJsonEvent;
import net.savantly.nexus.flow.api.events.submitFormJson.SubmitFormJsonEventData;
import net.savantly.nexus.flow.dom.form.Form;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmission;
import net.savantly.nexus.ga.dom.gaConnection.GAConnection;
import net.savantly.nexus.gaform.integtests.AbstractIntegrationTest;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecret;

public class Form_SubmissionListener_IntegTest extends AbstractIntegrationTest {

    @Autowired
    EventBusService eventBusService;

    @SpyBean
    Form_SubmissionListener listener;

    @Autowired
    FactoryService factoryService;

    @Autowired
    AttributeEncryptor attributeEncryptor;

    @Test
    void handleEvent() throws IOException {

        // given
        var organization = Organization.withName("test");

        // this secret is only used for testing
        // it's not a real secret
        var secret = OrganizationSecret.withName(organization, "test");
        secret.setEncryptedSecret(attributeEncryptor.convertToDatabaseColumn("Mj9h81s_ThW1XsrrpdF4CQ"));

        var ga = GAConnection.withRequiredArgs(organization, "test", "G-K8021YZYW4");
        ga.setApiKey(secret);
        ga.setDebug(true);

        var form = Form.withName(organization, "test");
        var mixin = factoryService.mixin(Form_updateGoogleAnalytics.class, form);
        mixin.act(ga, "test");

        var payload = "{\"test\":\"test\"}";
        var formSubmission = FormSubmission.withRequiredArgs(form, payload);
        var clientIP = "127.0.0.1";

        var request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader(anyString())).thenReturn("user-1");

        var event = new SubmitFormJsonEvent(SubmitFormJsonEventData.builder()
                .submission(formSubmission)
                .clientIP(clientIP)
                .request(request)
                .build());

        // when
        eventBusService.post(event);

        // then
        // no exception is thrown
        // make sure the listener is called
        Mockito.verify(listener).onApplicationEvent(event);

    }

}
