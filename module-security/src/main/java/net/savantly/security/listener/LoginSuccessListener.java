package net.savantly.security.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import lombok.RequiredArgsConstructor;

/**
 * This class represents an event listener that listens for successful
 * authentication events.
 */
@RequiredArgsConstructor
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent evt) {

        // no op

    }
}