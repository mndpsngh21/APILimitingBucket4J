package com.mandeep.ratelimiter.bucket4J.serviceImpl;

import org.springframework.stereotype.Service;

import com.mandeep.ratelimiter.bucket4J.GenericResponse;
import com.mandeep.ratelimiter.bucket4J.services.SampleService;

@Service
public class ServiceImpl implements SampleService{

	public final int MINIMUM_TIMEOUT=2000;
	
	@Override
	public GenericResponse sampleRequest(int timeout) {
		
		if(timeout==0)
		{
			timeout=MINIMUM_TIMEOUT;
		}
		
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GenericResponse genericResponse= new GenericResponse();
		genericResponse.setMessage(Constants.SAMPLE_MESSAGE);
		genericResponse.setSuccess(true);
		return genericResponse;
	}


}
