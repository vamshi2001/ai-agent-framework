package com.api.hub.llm.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.api.hub.ai.BackOffFactory;
import com.api.hub.auth.AutheticationHandler;
import com.api.hub.exception.ApiHubException;
import com.api.hub.http.HttpHandler;
import com.api.hub.http.url.HostReslover;
import com.api.hub.llm.LLMService;

import lombok.RequiredArgsConstructor;

@Component
@Scope(value = "prototype")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "llm.ollama.api.enable", havingValue = "true")
public class OllamaLLMService<R>  implements LLMService<R> {
	
	
	@Autowired
	private Environment env;
	
	private String name;
    
    @Autowired
    @Qualifier("${llm.ollama.api.httpHandler}")
    private HttpHandler handler;
    
    @Autowired
    @Qualifier("${llm.ollama.api.hostReslover}")
    private HostReslover hostReslover;
    
    @Autowired
    @Qualifier("${llm.ollama.api.autheticationHandler}")
    private AutheticationHandler autheticationHandler;
    
    public void init(String name) {
    	handler.init("ollama." + name );
    	hostReslover.init("ollama." + name );
    	autheticationHandler.init("ollama." + name );
    }

    private HttpHeaders defaultHeaders() throws ApiHubException {
        HttpHeaders headers = new HttpHeaders();;
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        return headers;
    }


    @Override
    public ResponseEntity<R> sendChatRequest(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
         
         return handler.getResponse(
        		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
        			    	.setPathParams("api/chat")
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
       			    	.setPathParams("api/generate")
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
    public ResponseEntity<R> generateTextEmbeddings(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
    	
    	Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>) (Class<?>) requestBody.getClass();
        
        return handler.getResponse(
       		 handler.sendRequest(handler.createRequest(responseClass, mapClass)
       			    	.setPathParams("api/embeddings")
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
	public ResponseEntity<R> generateImageFromText(Map<String, Object> requestBody, Class<R> responseClass)
			throws ApiHubException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<R> synthesizeSpeechFromText(Map<String, Object> requestBody, Class<R> responseClass)
			throws ApiHubException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<R> transcribeAudioFile(FileSystemResource audioFile, String model, Class<R> responseClass)
			throws ApiHubException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<R> generateImageEmbeddings(FileSystemResource imageFile, String model, Class<R> responseClass)
			throws ApiHubException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<R> generateTextFromImage(FileSystemResource imageFile, String model, Class<R> responseClass)
			throws ApiHubException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<R> transformImage(Map<String, Object> requestBody, Class<R> responseClass)
			throws ApiHubException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<R> processMultimodalRequest(Map<String, Object> requestBody, Class<R> responseClass)
			throws ApiHubException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<R> generateTextFromVideo(FileSystemResource videoFile, String model, Class<R> responseClass)
			throws ApiHubException {
		// TODO Auto-generated method stub
		return null;
	}
}
