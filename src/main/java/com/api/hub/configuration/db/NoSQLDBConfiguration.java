package com.api.hub.configuration.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
/**
 * Configuration class for setting up MongoDB connection for the AI-Agent Framework.
 * <p>
 * This configuration is conditionally activated only if the property <code>nosql.db.enable=true</code> is set
 * in the <code>application.properties</code> file.
 * <p>
 * It initializes and configures a MongoDB connection using custom parameters such as max connections,
 * wait time, timeouts, and the target database name. These settings ensure high-performance NoSQL
 * data access for AI Agents to store and retrieve structured and semi-structured data for processing,
 * training logs, configuration metadata, and agent history.
 * </p>
 * <p><b>Required Properties:</b></p>
 * <ul>
 *   <li><code>nosql.db.enable=true</code></li>
 *   <li><code>mongoDB.url</code> - MongoDB connection URL</li>
 *   <li><code>mongoDB.DBName</code> - Target MongoDB database name</li>
 *   <li><code>mongoDB.maxConnections</code> - Maximum number of connections in the pool</li>
 *   <li><code>mongoDB.minConnections</code> - Minimum number of idle connections</li>
 *   <li><code>mongoDB.maxWaitTime</code> - Maximum time to wait for a connection from the pool (in milliseconds)</li>
 *   <li><code>mongoDB.connectionTimeOut</code> - Connection timeout duration (in seconds)</li>
 *   <li><code>mongoDB.readTimeOut</code> - Read timeout duration (in seconds)</li>
 * </ul>
 *
 * <p><b>Usage in AI-Agent Framework:</b><br>
 * This configuration ensures that all NoSQL operations used by AI agents — including
 * dynamic knowledge storage, fine-tuned prompts, chat memory, and real-time data logs —
 * are performed on a high-performance MongoDB cluster with well-optimized connection settings.</p>
 *
 * @author 
 * @since 1.0
 */
@Configuration
@ConditionalOnProperty(name = "nosql.db.enable", havingValue = "true")
public class NoSQLDBConfiguration {

    @Value("${mongoDB.url}")
    private String mongoDBUrl;

    @Value("${mongoDB.DBName}")
    private String mongoDbName;

    @Value("${mongoDB.maxConnections}")
    private Integer mongoMaxConnnections;

    @Value("${mongoDB.minConnections}")
    private Integer mongoMinConnections;

    @Value("${mongoDB.maxWaitTime}")
    private Integer mongoMaxWaitTime;

    @Value("${mongoDB.connectionTimeOut}")
    private Integer mongoConnectionTimeOut;

    @Value("${mongoDB.readTimeOut}")
    private Integer mongoReadTimeOut;

    /**
     * Initializes and provides a MongoDatabase instance connected to the configured MongoDB cluster.
     *
     * @return the configured {@link com.mongodb.client.MongoDatabase} instance
     */
    @Bean
    public MongoDatabase getMongoConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new com.mongodb.ConnectionString(mongoDBUrl))
            .applyToConnectionPoolSettings(builder -> builder
                .maxSize(mongoMaxConnnections)
                .minSize(mongoMinConnections)
                .maxWaitTime(mongoMaxWaitTime, java.util.concurrent.TimeUnit.MILLISECONDS)
            )
            .applyToSocketSettings(builder -> builder
                .connectTimeout(mongoConnectionTimeOut, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(mongoReadTimeOut, java.util.concurrent.TimeUnit.SECONDS)
            )
            .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
            .build();

        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase(mongoDbName);
    }
}