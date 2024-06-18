package net.savantly.nexus.flow.dom.recaptcha;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

public class ReCaptchaAttemptService {
    private final int MAX_ATTEMPT = 4;
    private CacheManager cacheManager;
    private ConcurrentMapCache attemptsCache;

    public ReCaptchaAttemptService() {
        this.cacheManager = new ConcurrentMapCacheManager();
        attemptsCache = (ConcurrentMapCache) cacheManager.getCache("reCaptchaAttemptsCache");
    }

    public void reCaptchaSucceeded(String key) {
        attemptsCache.evict(key);
    }

    public void reCaptchaFailed(String key) {
        Integer attempts = attemptsCache.get(key, Integer.class);
        if (attempts == null) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        Integer attempts = attemptsCache.get(key, Integer.class);
        return attempts != null && attempts >= MAX_ATTEMPT;
    }
}
