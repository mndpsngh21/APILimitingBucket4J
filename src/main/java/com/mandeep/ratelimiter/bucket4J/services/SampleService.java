package com.mandeep.ratelimiter.bucket4J.services;

import com.mandeep.ratelimiter.bucket4J.GenericResponse;

public interface SampleService {

	public GenericResponse sampleRequest(int timeout);
	
	
}
