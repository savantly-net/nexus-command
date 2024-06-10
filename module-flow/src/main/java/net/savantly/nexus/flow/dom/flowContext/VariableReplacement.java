package net.savantly.nexus.flow.dom.flowContext;

import java.util.HashMap;
import java.util.Map;

public class VariableReplacement {


    /**
     * Replaces variables in the input string with values from the context
     * Uses the format ${variableName} for variables and {{ secrets.secretName }} for secrets
     * 
     * @param input
     * @param context
     * @return
     */
    public static String replaceVariables(String input, FlowContext context) {

        Map<String, Object> variables = context.getVariables();
        Map<String, String> secrets = context.getSecrets();

        String result = input;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }

        // should be like github actions secrets format {{ secrets.secretName }}
        for (Map.Entry<String, String> entry : secrets.entrySet()) {
            result = result.replaceAll("\\{\\{\\s*secrets\\." + entry.getKey() + "\\s*\\}\\}", entry.getValue());
        }
        return result;
    }
    
}
