package net.savantly.nexus.flow.api.events.submitFormJson;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

public class SubmitFormJsonEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    @Getter
    private final SubmitFormJsonEventData source;

    public SubmitFormJsonEvent(SubmitFormJsonEventData source) {
        super(source);
        this.source = source;
    }

}
