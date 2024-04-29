package net.savantly.ai.languagetools;

import dev.langchain4j.model.chat.ChatLanguageModel;

public interface LanguageToolModel extends ChatLanguageModel {

    default ChatLanguageModel asChatLanguageModel() {
        return this;
    }
}