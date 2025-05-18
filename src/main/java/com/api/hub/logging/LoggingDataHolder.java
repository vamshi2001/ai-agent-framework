package com.api.hub.logging;

/**
 * Thread-safe holder for {@link LoggingData} objects used in the AI-Agent Framework.
 * <p>
 * This class leverages {@link ThreadLocal} to maintain and isolate logging context data
 * (such as request metadata, session identifiers, agent name, or trace ID) for each thread
 * during the processing of a request. It ensures that logging data remains consistent and
 * isolated across concurrent requests in multi-threaded environments such as web servers or schedulers.
 * </p>
 *
 * <p><b>Typical Usage:</b></p>
 * <ul>
 *   <li>To populate logging context at the start of a request or operation.</li>
 *   <li>To fetch and enrich logs with agent-specific metadata during processing.</li>
 *   <li>To clean up logging context after request completion to avoid memory leaks.</li>
 * </ul>
 *
 * <p><b>Example:</b></p>
 * <pre>{@code
 * LoggingData data = new LoggingData();
 * data.setAgentName("ChatBotAgent");
 * data.setRequestId(UUID.randomUUID().toString());
 * LoggingDataHolder.set(data);
 *
 * // Later in processing:
 * log.info("Agent request received: {}", LoggingDataHolder.get().getRequestId());
 *
 * // At the end of request:
 * LoggingDataHolder.clear();
 * }</pre>
 *
 * <p>This class plays a critical role in centralized logging, auditing, and tracing of agent operations,
 * especially in distributed and asynchronous AI-agent workflows.</p>
 *
 * @see LoggingData

 * @since 1.0
 */
public class LoggingDataHolder {

    /**
     * Thread-local container for holding LoggingData specific to the current thread.
     */
    private static ThreadLocal<LoggingData> pojoHolder = new ThreadLocal<>();

    /**
     * Retrieves the {@link LoggingData} instance for the current thread. If not already present,
     * a new instance is created and set.
     *
     * @return the current thread's {@link LoggingData} instance
     */
    public static LoggingData get() {
        LoggingData pojo = pojoHolder.get();
        if (pojo == null) {
            pojo = new LoggingData();
        }
        pojoHolder.set(pojo);
        return pojo;
    }

    /**
     * Sets the {@link LoggingData} instance for the current thread.
     *
     * @param data the {@link LoggingData} to associate with the current thread
     */
    public static void set(LoggingData data) {
        pojoHolder.set(data);
    }

    /**
     * Clears the {@link LoggingData} associated with the current thread,
     * preventing memory leaks and stale data reuse in thread pools.
     */
    public static void clear() {
        pojoHolder.remove();
    }
}