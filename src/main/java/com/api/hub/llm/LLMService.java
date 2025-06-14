package com.api.hub.llm;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import com.api.hub.exception.ApiHubException;

import java.util.Map;

/**
 * Generic interface for interacting with various Generative AI model types across platforms.
 * Supports multimodal input-output operations such as text generation, speech processing, image generation, and embeddings.
 *
 * @param <R> the response type expected from the AI platform
 */
public interface LLMService<R> {

    // -------------------- TEXT TO TEXT --------------------

    /**
     * Sends a chat-like conversation prompt and receives a structured response.
     *
     * @param requestBody   the chat-style request containing model, messages, and parameters
     * @param responseClass the class of the response object
     * @return the generated chat response
     */
    ResponseEntity<R> sendChatRequest(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException;

    /**
     * Sends a plain text prompt to the model and gets a completion.
     *
     * @param requestBody   the request body with text prompt and options
     * @param responseClass the class of the response object
     * @return the text completion response
     */
    ResponseEntity<R> sendPromptRequest(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException;

    // -------------------- TEXT TO IMAGE --------------------

    /**
     * Generates an image based on a text prompt and configuration parameters.
     *
     * @param requestBody   the request body containing text prompt and image generation options
     * @param responseClass the class of the response object
     * @return the image generation response
     */
    ResponseEntity<R> generateImageFromText(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException;

    // -------------------- TEXT TO SPEECH --------------------

    /**
     * Converts input text into audio (speech synthesis).
     *
     * @param requestBody   the request containing text and voice settings
     * @param responseClass the class of the response object (or binary stream)
     * @return the generated audio data
     */
    ResponseEntity<R> synthesizeSpeechFromText(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException;

    // -------------------- SPEECH TO TEXT --------------------

    /**
     * Transcribes speech from an audio file into text.
     *
     * @param audioFile     the audio file to transcribe
     * @param model         the speech recognition model
     * @param responseClass the class of the response object
     * @return the transcribed text
     */
    ResponseEntity<R> transcribeAudioFile(FileSystemResource audioFile, String model, Class<R> responseClass) throws ApiHubException;

    // -------------------- TEXT TO EMBEDDING --------------------

    /**
     * Generates vector embeddings for a given input text.
     *
     * @param requestBody   the input text and model config
     * @param responseClass the class of the embedding result
     * @return the text embeddings
     */
    ResponseEntity<R> generateTextEmbeddings(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException;

    // -------------------- IMAGE TO EMBEDDING --------------------

    /**
     * Generates vector embeddings from an image file or data.
     *
     * @param imageFile     the image file
     * @param model         the embedding model
     * @param responseClass the response class
     * @return the image embeddings
     */
    ResponseEntity<R> generateImageEmbeddings(FileSystemResource imageFile, String model, Class<R> responseClass) throws ApiHubException;

    // -------------------- IMAGE TO TEXT --------------------

    /**
     * Analyzes the image and returns text-based output (e.g., caption, OCR, description).
     *
     * @param imageFile     the image to be analyzed
     * @param model         the vision model
     * @param responseClass the response class
     * @return the image-to-text response
     */
    ResponseEntity<R> generateTextFromImage(FileSystemResource imageFile, String model, Class<R> responseClass) throws ApiHubException;

    // -------------------- IMAGE TO IMAGE --------------------

    /**
     * Transforms an image based on a model (e.g., style transfer, variation).
     *
     * @param requestBody   the request containing image, prompt, and style configs
     * @param responseClass the class of the response
     * @return the image transformation result
     */
    ResponseEntity<R> transformImage(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException;

    // -------------------- MULTIMODAL (TEXT + IMAGE) TO TEXT --------------------

    /**
     * Processes both image and text input for multimodal reasoning (e.g., visual Q&A).
     *
     * @param requestBody   the request body containing text and image reference
     * @param responseClass the response type
     * @return the multimodal reasoning output
     */
    ResponseEntity<R> processMultimodalRequest(Map<String, Object> requestBody, Class<R> responseClass) throws ApiHubException;

    // -------------------- VIDEO TO TEXT --------------------

    /**
     * Extracts textual insights or transcription from a video file.
     *
     * @param videoFile     the input video
     * @param model         the video understanding model
     * @param responseClass the class of the response
     * @return extracted or generated text from video
     */
    ResponseEntity<R> generateTextFromVideo(FileSystemResource videoFile, String model, Class<R> responseClass) throws ApiHubException;
}