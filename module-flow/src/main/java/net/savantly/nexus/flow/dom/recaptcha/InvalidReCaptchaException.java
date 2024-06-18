package net.savantly.nexus.flow.dom.recaptcha;

public class InvalidReCaptchaException extends RuntimeException {

    public InvalidReCaptchaException(String message) {
        super(message);
    }
}
