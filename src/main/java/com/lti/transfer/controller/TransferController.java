package com.lti.transfer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.lti.transfer.domain.DetailResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RestController
@RequestMapping(value = "transfer")
@RefreshScope
@CrossOrigin(origins = "http://localhost:4200")
public class TransferController {
	
	
	@Autowired
	private WebClient.Builder builder;
	
	@Value(value = "${greeting}")
	private String test;
	
	@Value(value = "${testValue}")
	private String testValue;
	
	@Value(value = "${my.list.values}")
	private List<String> list;
	
	@Value(value = "#{${dbValues}}")
	private Map<String, String> dbValues;
	
	@GetMapping(value = "/getDetailsFromBgv")
	@HystrixCommand(fallbackMethod = "getResponseFromFallbackMethod")
	public DetailResponse getBgvResponse() {
		
		DetailResponse detailResponse = new DetailResponse();
		
		try {
			detailResponse = builder.build()
					.get()
					.uri("http://zuul-api-gateway/bgv-module/bgv/getBgvResponse")
					.retrieve()
					.bodyToMono(DetailResponse.class)
					.block();
			detailResponse.setMap(dbValues);
		}catch (Exception e) {
			detailResponse.setError(true);
			detailResponse.setErrorMessage(e.getMessage());
		}
		return detailResponse;
	}
	
	public DetailResponse getResponseFromFallbackMethod() {
		
		DetailResponse detailResponse = new DetailResponse();
		detailResponse.setError(true);
		detailResponse.setErrorMessage("something went wrong. so response came from fallback method.");
		return detailResponse;
		
	}
	
	@GetMapping(value = "/getTestResponse")
	public Object getTestResponse() {
		return dbValues + " " + testValue;
	}
	
	@GetMapping(value = "/hello")
	public Object getHello() {
		return "Hello world again..";
	}
	

}
