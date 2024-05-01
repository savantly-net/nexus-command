package net.savantly.ai.languagetools;

public class PromptBuilder {

    public static String format(HasPrompt... prompt) {
        var sb = new StringBuilder();
        for (var p : prompt) {
            sb.append(p.getPrompt());
        }
        return sb.toString();
    }
}
