package com.mandeep.ratelimiter.bucket4J.serviceImpl;

import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    public boolean isRateLimitRequired(String url) {
        boolean isRequired= ((null != url) &&
            (url.endsWith("/api/getData") ||
             url.endsWith("/api/getAnotherData")));
        String state=isRequired?" checked":"not checked";
        System.out.println("URL "+url+" will be "+state+" by rateLimitChecker");
        
        return isRequired;
    }

   
 }
