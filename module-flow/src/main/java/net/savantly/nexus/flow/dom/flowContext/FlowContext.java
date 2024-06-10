package net.savantly.nexus.flow.dom.flowContext;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class FlowContext {
    private Map<String, Object> variables = new HashMap<>();
    private Map<String, String> secrets = new HashMap<>();


    public static FlowContext empty() {
        return new FlowContext();
    }

    // ****** VARIABLES ******
    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }

    public Object getVariable(String key) {
        return variables.get(key);
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }



    // ****** SECRETS ******

    public void setSecret(String key, String value) {
        secrets.put(key, value);
    }

    public String getSecret(String key) {
        return secrets.get(key);
    }

    public Map<String, String> getSecrets() {
        return secrets;
    }

    public void setSecrets(Map<String, String> secrets) {
        this.secrets = secrets;
    }



    public String serialize(ObjectMapper mapper) {
        var context = new HashMap<String, Object>();
        context.put("variables", variables);
        try {
            return mapper.writeValueAsString(context);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize flow context", e);
        }
    }
}
