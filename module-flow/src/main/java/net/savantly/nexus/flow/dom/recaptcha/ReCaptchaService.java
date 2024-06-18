package net.savantly.nexus.flow.dom.recaptcha;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReCaptchaService {

    private final ReCaptchaAttemptService reCaptchaAttemptService;
    private final String recaptchEndpoint;
    private final RestOperations restTemplate = new RestTemplate();

    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    public void processResponse(String response, String reacaptchSecret, float threshold, String clientIP,
            String action) {
        if (reCaptchaAttemptService.isBlocked(clientIP)) {
            throw new InvalidReCaptchaException("Client exceeded maximum number of failed attempts");
        }
        if (!responseSanityCheck(response)) {
            throw new InvalidReCaptchaException("Response contains invalid characters");
        }

        URI verifyUri = URI.create(String.format(
                "%s?secret=%s&response=%s&remoteip=%s",
                recaptchEndpoint,
                reacaptchSecret, response, clientIP));

        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

        if (Objects.isNull(googleResponse) || !googleResponse.isSuccess()
                || (Objects.nonNull(action) && (!googleResponse.getAction().equals(action)
                        || googleResponse.getScore() < threshold))) {
            if (Objects.nonNull(googleResponse) && googleResponse.hasClientError()) {
                reCaptchaAttemptService.reCaptchaFailed(clientIP);
            }
            throw new InvalidReCaptchaException("reCaptcha was not successfully validated");
        }
        reCaptchaAttemptService.reCaptchaSucceeded(clientIP);
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
}