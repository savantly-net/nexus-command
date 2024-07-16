package net.savantly.nexus.flow.dom.flowContext;

import java.io.IOException;
import java.util.Objects;

import com.github.jknack.handlebars.Handlebars;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class VariableReplacement {

    final static Handlebars handlebars = new Handlebars();

    /**
     * Replaces variables in the input string with values from the context
     * Uses the format ${variableName} for variables and {{ secrets.secretName }}
     * for secrets
     * 
     * @param input
     * @param context
     * @return
     */
    public static String replaceVariables(String input, FlowContext context) {

        if (Objects.isNull(input) || input.isEmpty()) {
            return input;
        }

        try {
            return handlebars.compileInline(input).apply(context);
        } catch (IOException e) {
            log.error("Failed to compile handlebars template", e);
            throw new IllegalArgumentException("Failed to compile handlebars template. " + e.getMessage());
        }
    }

}
