package com.api.hub.llm.handler;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.api.hub.ai.BackOffFactory;
import com.api.hub.exception.ApiHubException;
import com.api.hub.http.AutheticationHandler;
import com.api.hub.http.HttpHandler;
import com.api.hub.http.HttpRequest;
import com.api.hub.http.handler.SpringRestTempletHandler;
import com.api.hub.http.url.HostReslover;
import com.api.hub.llm.LLMService;
import com.api.hub.llm.pojo.LLMApiProperties;

import jakarta.annotation.PostConstruct;

import java.util.Map;

@Component
@Scope(value = "prototype")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "llm.openai.api.enable", havingValue = "true")
public class OpenApiLLMService<R> implements LLMService<R> {

	//backoff 
	// authetication handler
	// host reslover
	
	@Autowired
	private Environment env;
	
	private String name;
	
    @Autowired
    private LLMApiProperties config;
    
    @Autowired
    @Qualifier("${llm.openai.api.httpHandler}")
    private HttpHandler handler;
    
    @Autowired
    @Qualifier("${llm.openai.api.hostReslover}")
    private HostReslover hostReslover;
    
    @Autowired
    @Qualifier("${llm.openai.api.autheticationHandler}")
    private AutheticationHandler autheticationHandler;
    
    public void init(String name) {
    	handler.init("openapi." + name );
    	hostReslover.init("openapi." + name );
    	autheticationHandler.init("openapi." + name );
    }

    private HttpHeaders defaultHeaders() throws ApiHubException {
        HttpHeaders headers = new HttpHeaders();;
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (config.getOrganization() != null) {
            headers.set("OpenAI-Organization", config.getOrganization());
        }
        return headers;
    }

    private String buildUrl(String path) throws ApiHubException {
        return config.getBaseUrl() + path;
    }

    @Override
    public ResponseEntity<R> sendChatRequest(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
         
         return handler.getResponse(
        		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
        			    	.setPathParams("/chat/completions")
        			    	.setSpringHeaders(defaultHeaders())
        			    	.setRequestBody(requestBody)
        			    	.setHttpMethod(HttpMethod.POST)
        			    	.setAuthetication(autheticationHandler)
        			    	.setHostReslover(hostReslover)
        			    	.setBackoff(BackOffFactory.createBackOff(env, name))
        				 )
        		 
        		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> sendPromptRequest(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/completions")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(requestBody)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> generateEmbeddings(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/embeddings")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(requestBody)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> transcribeAudioFile(FileSystemResource audioFile, String model, Class<R> responseClass) throws ApiHubException {
    	
    	HttpHeaders headers = defaultHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("file", audioFile);
        formData.add("model", model);
    	Class<MultiValueMap<String, Object>> mapClass = (Class<MultiValueMap<String, Object>>) (Class<?>) formData.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/audio/transcriptions")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(formData)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> synthesizeSpeechFromText(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/audio/speech")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(requestBody)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> generateImageFromText(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/images/generations")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(requestBody)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> moderateContent(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/moderations")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(requestBody)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> startFineTuningJob(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/fine_tuning/jobs")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(requestBody)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> uploadFile(FileSystemResource file, String purpose, Class<R> responseClass) throws ApiHubException {
    	
    	HttpHeaders headers = defaultHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("file", file);
        formData.add("purpose", purpose);
    	Class<MultiValueMap<String, Object>> mapClass = (Class<MultiValueMap<String, Object>>) (Class<?>) formData.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/files")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(formData)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }

    @Override
    public ResponseEntity<R> createAssistant(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("/assistants")
       			    	.setSpringHeaders(defaultHeaders())
       			    	.setRequestBody(requestBody)
       			    	.setHttpMethod(HttpMethod.POST)
       			    	.setAuthetication(autheticationHandler)
       			    	.setHostReslover(hostReslover)
       			    	.setBackoff(BackOffFactory.createBackOff(env, name))
       				 )
       		 
       		 ).getResponse();
    }
}