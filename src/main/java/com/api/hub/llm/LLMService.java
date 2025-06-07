package com.api.hub.llm;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import com.api.hub.exception.ApiHubException;

import java.util.Map;

/**
 * Interface for interacting with various LLM platform capabilities.
 * These methods are generic and can be implemented for platforms like OpenAI, Claude, Ollama, Azure OpenAI, etc.
 */
public interface LLMService<R> {

    /**
     * Sends a prompt to a chat-based model and receives a conversational response.
     *
     * @param requestBody the request body containing the model, messages, and parameters
     * @return the response from the chat model
     */
    default ResponseEntity<R> sendChatRequest(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Sends a text prompt to a completion-based model and receives a response.
     *
     * @param requestBody the request body containing the model, prompt, and parameters
     * @return the generated completion
     */
    default ResponseEntity<R> sendPromptRequest(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Generates text embeddings for the provided input.
     *
     * @param requestBody the request body containing input text and model
     * @return the embeddings vector response
     */
    default ResponseEntity<R> generateEmbeddings(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Transcribes speech from an audio file to text.
     *
     * @param audioFile the audio file to be transcribed
     * @return the transcribed text
     */
    default ResponseEntity<R> transcribeAudioFile(FileSystemResource audioFile, String model, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Converts input text to spoken audio.
     *
     * @param requestBody the request body containing text and model parameters
     * @return the generated audio as a byte array
     */
    default ResponseEntity<R> synthesizeSpeechFromText(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Generates images based on a text description.
     *
     * @param requestBody the request body containing the prompt and image parameters
     * @return the image generation response
     */
    default ResponseEntity<R> generateImageFromText(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Evaluates content for potential harmful or unsafe material.
     *
     * @param requestBody the request body containing input text
     * @return the moderation result
     */
    default ResponseEntity<R> moderateContent(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Initiates a fine-tuning job for a model using custom training data.
     *
     * @param requestBody the request body containing training data and configuration
     * @return the fine-tuning job status response
     */
    default ResponseEntity<R> startFineTuningJob(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Uploads a file for training, fine-tuning, or other platform-specific purposes.
     *
     * @param file    the file to upload
     * @param purpose the purpose for which the file is being uploaded (e.g., "fine-tune")
     * @return the upload status or file ID
     */
    default ResponseEntity<R> uploadFile(FileSystemResource file, String purpose, Class<R> responseClass) throws ApiHubException {
        return null;
    }

    /**
     * Creates a virtual assistant (e.g., for multi-turn interactions, tools integration).
     *
     * @param requestBody the request body defining the assistant configuration
     * @return the created assistant details
     */
    default ResponseEntity<R> createAssistant(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException {
        return null;
    }
}