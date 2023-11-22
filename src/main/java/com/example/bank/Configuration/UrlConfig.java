package com.example.bank.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UrlConfig {
	   @Bean
	    public WebClient.Builder webClientBuilder(){
	        return WebClient.builder();
	    }

	    @Bean
	    public WebClient webClientForTopUp(WebClient.Builder webClientBuilder)
	    {
	        return webClientBuilder.baseUrl("http://192.168.1.43:9090/mtelecom/").build();
	    }
	
}
