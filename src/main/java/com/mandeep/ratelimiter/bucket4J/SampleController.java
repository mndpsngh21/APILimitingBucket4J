package com.mandeep.ratelimiter.bucket4J;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mandeep.ratelimiter.bucket4J.services.SampleService;

@RestController
@RequestMapping("/api")
public class SampleController {

@Autowired	
private SampleService sampleService;
	
	
@GetMapping("/getData")
@ResponseBody
public GenericResponse callServerForData(HttpServletRequest request)
{
	System.out.println("Received Request from IP::"+request.getRemoteAddr());

	return sampleService.sampleRequest(0);	
}


@GetMapping("/getAnotherData")
@ResponseBody
public GenericResponse callServerForAnotherData(HttpServletRequest request)
{
System.out.println("Received Request from IP::"+request.getRemoteAddr());

return sampleService.sampleRequest(0);	
}

@GetMapping("/freeAPI")
@ResponseBody
public GenericResponse callServerFreeAPI(HttpServletRequest request)
{
	System.out.println("Received Request from IP::"+request.getRemoteAddr());

	return sampleService.sampleRequest(0);	
}
	

}
