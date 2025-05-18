package com.api.hub.logging;

import java.util.Date;

import com.api.hub.ai.cache.Cache;

import lombok.Data;

/**
 * Represents structured logging information for the AI-Agent Framework.
 * <p>
 * This data class encapsulates metadata and message details used for enriched logging
 * across AI agent workflows and services. It includes identifiers to correlate logs
 * within conversations and transactions, enabling detailed tracing and analysis.
 * </p>
 * 
 * <p><b>Fields include:</b></p>
 * <ul>
 *   <li>{@code applicationName}: Identifies the application or agent generating the log.</li>
 *   <li>{@code conversationId}: Correlates logs within a specific conversation context.</li>
 *   <li>{@code transactionId}: Correlates logs within a specific transaction or request.</li>
 *   <li>{@code marker}: A custom log marker, e.g., "WORKFLOW" or other domain-specific tags {@link com.api.hub.ai.constants.MarkerConstants}.</li>
 *   <li>{@code logLevel}: Log severity level (e.g., INFO, DEBUG, ERROR).</li>
 *   <li>{@code className}: The source class or component generating the log.</li>
 *   <li>{@code logMessage}: The formatted log message content.</li>
 * </ul>
 * 
 * <p><b>Serialization:</b></p>
 * <p>The {@link #toString()} method serializes this object into a JSON-like string format,
 * including a timestamp field with the current date/time. All string fields are properly
 * escaped to maintain JSON integrity.</p>
 * 
 * <p><b>Usage:</b></p>
 * <ul>
 *   <li>Used internally by logging components such as {@link LoggingEncoder} to structure logs.</li>
 *   <li>Supports thread-local management via {@link LoggingDataHolder} for agent-contextual logging.</li>
 * </ul>
 * 
 
 * @see com.api.hub.ai.constants.MarkerConstants
 * @since 1.0
 */
@Data
public class LoggingData {

    /** Application or agent name producing the log entry. */
    private String applicationName;

    /** Conversation ID to correlate logs within a single interaction session. */
    private String conversationId;

    /** Transaction ID to correlate logs within a particular request or transaction. */
    private String transactionId;

    /** Marker indicating the domain or subsystem (e.g., "WORKFLOW"). */
    private String marker;

    /** Severity level of the log (INFO, WARN, ERROR, etc.). */
    private String logLevel;

    /** Name of the class or component that generated the log event. */
    private String className;

    /** Detailed log message content. */
    private String logMessage;

    /**
     * Serializes this logging data into a JSON-like string representation.
     * <p>
     * Includes a timestamp of when this method is called, along with all fields,
     * properly escaped for JSON safety.
     * </p>
     * 
     * @return JSON-like string representing this log entry
     */
    @Override
    public String toString() {
        return String.format(
            "{\n" +
            "  \"time\": \"%s\"," +
            "  \"applicationName\": \"%s\"," +
            "  \"marker\": \"%s\"," +
            "  \"logLevel\": \"%s\"," +
            "  \"conversationId\": \"%s\"," +
            "  \"transactionId\": \"%s\"," +
            "  \"className\": \"%s\"," +
            "  \"logMessage\": \"%s\"" +
            "}", 
            new Date().toString(),
            escapeJson(applicationName),
            escapeJson(marker),
            escapeJson(logLevel),
            escapeJson(conversationId),
            escapeJson(transactionId),
            escapeJson(className),
            escapeJson(logMessage)
        );
    }

    /**
     * Escapes special characters in a string for safe inclusion in JSON.
     *
     * @param value the raw string value
     * @return the escaped string, or {@code null} if input is null
     */
    private String escapeJson(String value) {
        if (value == null) return null;
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    /**
     * Default constructor.
     */
    public LoggingData() {
    }

    /**
     * Constructs a LoggingData instance with specified application, conversation, and transaction IDs.
     * 
     * @param applicationName the application or agent name
     * @param conversationId the conversation identifier
     * @param transactionId the transaction identifier
     */
    public LoggingData(String applicationName, String conversationId, String transactionId) {
        super();
        this.applicationName = applicationName;
        this.conversationId = conversationId;
        this.transactionId = transactionId;
    }

    /**
     * Creates a shallow copy of this LoggingData instance.
     * 
     * @return a new LoggingData instance with the same application, conversation, and transaction IDs
     */
    public LoggingData clone() {
        return new LoggingData(applicationName, conversationId, transactionId);
    }
}