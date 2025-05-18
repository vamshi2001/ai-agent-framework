package com.api.hub.logging;

import java.util.List;

import org.slf4j.Marker;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;

/**
 * Custom Logback encoder for transforming {@link ILoggingEvent} instances into
 * enriched structured log messages within the AI-Agent Framework.
 *
 * <p>This encoder extracts relevant contextual information from log events and
 * integrates it with metadata stored in {@link LoggingDataHolder}. It ensures that
 * each log message includes agent-specific details such as class name, log level,
 * log marker, and formatted message, which is crucial for centralized logging, monitoring,
 * and troubleshooting in agent-based distributed systems.</p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Fetch agent logging metadata via {@link LoggingDataHolder}.</li>
 *   <li>Extract and include structured information such as log level, class name, and markers.</li>
 *   <li>Produce log entries in a consistent, byte-encoded format.</li>
 * </ul>
 *
 * <p><b>Typical Usage:</b></p>
 * <ul>
 *   <li>Configured as the encoder in a Logback appender (e.g., FileAppender, ConsoleAppender).</li>
 *   <li>Used to enhance observability of individual agents in multi-agent workflows.</li>
 * </ul>
 *
 * <p><b>Example Log Output (as String):</b></p>
 * <pre>
 * [INFO] | AgentClass: com.agent.ChatBotAgent | Marker: WORKFLOW | Message: Agent initialized successfully
 * </pre>
 *
 * @see LoggingDataHolder
 * @see LoggingData
 * @see ch.qos.logback.core.encoder.EncoderBase
 * @author 
 * @since 1.0
 */
public class LoggingEncoder extends EncoderBase<ILoggingEvent> {

    /**
     * Returns the byte representation of the log header.
     * <p>
     * This implementation currently returns {@code null} as no header is required.
     *
     * @return header bytes or {@code null}
     */
    @Override
    public byte[] headerBytes() {
        return null;
    }

    /**
     * Encodes the given {@link ILoggingEvent} into a structured log format
     * by populating and serializing a {@link LoggingData} object.
     *
     * @param event the logging event to encode
     * @return the encoded byte array representation of the structured log
     */
    @Override
    public byte[] encode(ILoggingEvent event) {
        LoggingData data = LoggingDataHolder.get();

        data.setClassName(event.getLoggerName());
        data.setLogLevel(event.getLevel().toString());

        List<Marker> markerList = event.getMarkerList();
        String marker = "WORKFLOW";
        if (!markerList.isEmpty()) {
            marker = markerList.get(0).getName();
        }
        data.setMarker(marker);
        data.setLogMessage(event.getFormattedMessage());

        return data.toString().getBytes();
    }

    /**
     * Returns the byte representation of the log footer.
     * <p>
     * This implementation currently returns {@code null} as no footer is needed.
     *
     * @return footer bytes or {@code null}
     */
    @Override
    public byte[] footerBytes() {
        return null;
    }
}