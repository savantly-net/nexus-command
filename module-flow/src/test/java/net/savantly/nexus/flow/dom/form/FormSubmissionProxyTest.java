package net.savantly.nexus.flow.dom.form;

import java.time.ZonedDateTime;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.savantly.nexus.organizations.dom.organization.Organization;

class FormSubmissionProxyTest {

    @Test
    public void testSerialization() throws JsonProcessingException {

        var objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        var organization = Organization.withName("organizationName");
        var name = "formName";
        var form = Form.withName(organization, name);
        form.setName("formName");
        var payload = new HashMap<String, Object>();
        payload.put("key", "value");
        payload.put("key2", ZonedDateTime.now());

        objectMapper.writeValueAsString(payload);
    }
}
