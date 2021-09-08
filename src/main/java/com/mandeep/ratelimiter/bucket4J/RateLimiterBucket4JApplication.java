package com.mandeep.ratelimiter.bucket4J;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RateLimiterBucket4JApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimiterBucket4JApplication.class, args);
	}

}
