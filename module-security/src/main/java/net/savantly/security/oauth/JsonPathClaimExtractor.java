package net.savantly.security.oauth;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import org.springframework.security.oauth2.core.ClaimAccessor;

import java.util.List;
import java.util.Map;

@Log4j2
public class JsonPathClaimExtractor {

    public static List<String> extractRoles(ClaimAccessor claimAccessor, String jsonPathExpression) {
        Map<String, Object> claims = claimAccessor.getClaims();
        log.debug("extracting roles from claims: {}", claims);
        try {
            return JsonPath.read(claims, jsonPathExpression);
        } catch (Exception e) {
            log.error("error extracting roles from claims", e);
            return null;
        }
    }
}
