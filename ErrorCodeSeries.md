# Error Code Reference Guide

This document outlines the categorized error codes used in the system, their ranges, and detailed descriptions.

---

## Error Code Ranges and Categories

**1000–1999**: Input / Validation

**2000–2999**: Authentication / Session

**3000–3999**: API / Service Calls

**4000–4999**: Database Errors

**5000–5999**: Model / AI-related

**6000–6999**: Configuration Errors

**7000–7999**: Network / Timeouts

**8000–8999**: Internal Server Errors

**9000–9999**: Unknown / Generic

---

## 1000–1999: Input / Validation Errors

Errors triggered by invalid user input, missing data, or validation issues.

* **1001 `INVALID_INPUT`**: Input format is invalid or unparsable.
* **1002 `MISSING_REQUIRED_FIELD`**: Required field is missing.
* **1003 `INVALID_DATA_TYPE`**: Field value type mismatch.
* **1004 `VALUE_OUT_OF_RANGE`**: Input exceeds allowed limits.
* **1005 `UNSUPPORTED_INPUT_FORMAT`**: Input format not accepted.
* **1006 `EMPTY_INPUT`**: Input field or payload is empty.
* **1007 `INVALID_ENUM_VALUE`**: Value not in allowed enum list.
* **1008 `XSS_DETECTED`**: Potential XSS attack detected.
* **1009 `JSON_PARSE_ERROR`**: Malformed JSON input.
* **1010 `MALFORMED_REQUEST`**: Request structure is incorrect.

---

## 2000–2999: Authentication / Session Errors

Errors related to user authentication, tokens, and session handling.

* **2001 `UNAUTHORIZED_ACCESS`**: User not logged in or no token.
* **2002 `TOKEN_EXPIRED`**: Authentication token expired.
* **2003 `INVALID_TOKEN`**: Token invalid or tampered.
* **2004 `SESSION_NOT_FOUND`**: Session missing or expired.
* **2005 `INVALID_CREDENTIALS`**: Incorrect username/password.
* **2006 `ACCESS_DENIED`**: Permission denied for operation.
* **2007 `TOKEN_SIGNATURE_INVALID`**: Token signature invalid.
* **2008 `MULTIPLE_SESSIONS_BLOCKED`**: User logged in elsewhere.
* **2009 `LOGIN_ATTEMPT_LIMIT_EXCEEDED`**: Too many failed logins.
* **2010 `AUTH_PROVIDER_ERROR`**: OAuth or external auth failure.

---

## 3000–3999: API / Service Call Errors

Failures while interacting with APIs or external/internal services.

* **3001 `API_NOT_FOUND`**: API endpoint does not exist.
* **3002 `EXTERNAL_SERVICE_FAILURE`**: Third-party API failure.
* **3003 `SERVICE_UNAVAILABLE`**: Service is down.
* **3004 `API_TIMEOUT`**: API did not respond in time.
* **3005 `KAFKA_PUBLISH_FAILED`**: Kafka publish failed.
* **3006 `EMAIL_DELIVERY_FAILED`**: Email sending failed.
* **3007 `WEBHOOK_FAILURE`**: Outgoing webhook failed.
* **3008 `RATE_LIMIT_EXCEEDED`**: API rate limit exceeded.
* **3009 `UNSUPPORTED_HTTP_METHOD`**: HTTP method not supported.
* **3010 `BAD_GATEWAY`**: Gateway/proxy error.

---

## 4000–4999: Database Errors

Errors related to database connectivity and operations.

* **4001 `DB_CONNECTION_FAILED`**: Could not connect to DB.
* **4002 `DB_INSERT_FAILED`**: Insert operation failed.
* **4003 `DB_UPDATE_FAILED`**: Update operation failed.
* **4004 `DB_DELETE_FAILED`**: Delete operation failed.
* **4005 `DB_QUERY_TIMEOUT`**: Query timeout.
* **4006 `RECORD_NOT_FOUND`**: Data record missing.
* **4007 `DUPLICATE_ENTRY`**: Unique constraint violation.
* **4008 `TRANSACTION_ROLLBACK`**: Transaction rolled back.
* **4009 `DATA_INTEGRITY_VIOLATION`**: Foreign key or constraint error.
* **4010 `INVALID_DB_CONFIG`**: Bad DB configuration.

---

## 5000–5999: Model / AI-related Errors

Issues from AI model usage and integration.

* **5001 `MODEL_NOT_LOADED`**: AI model not initialized.
* **5002 `MODEL_RESPONSE_TIMEOUT`**: Model inference timed out.
* **5003 `MODEL_INFERENCE_ERROR`**: Inference failure.
* **5004 `PROMPT_GENERATION_FAILED`**: Failed to create LLM prompt.
* **5005 `EMBEDDING_FAILED`**: Text embedding failed.
* **5006 `VECTOR_SEARCH_ERROR`**: Vector DB query failed.
* **5007 `CONTEXT_TOO_LARGE`**: Prompt too big.
* **5008 `LLM_API_RATE_LIMIT`**: Rate limit exceeded on LLM.
* **5009 `INVALID_LLM_RESPONSE`**: Unexpected LLM output.
* **5010 `MISSING_TRAINING_DATA`**: Training data unavailable.

---

## 6000–6999: Configuration Errors

Errors caused by incorrect or missing configuration.

* **6001 `CONFIG_NOT_FOUND`**: Required config missing.
* **6002 `INVALID_CONFIG_VALUE`**: Config value invalid.
* **6003 `MISSING_ENV_VARIABLE`**: Environment variable missing.
* **6004 `UNSUPPORTED_DEPLOYMENT_MODE`**: Invalid run profile.
* **6005 `YAML_PARSE_ERROR`**: Config file syntax error.
* **6006 `SECURITY_POLICY_MISSING`**: Security policy missing.
* **6007 `MODULE_NOT_ENABLED`**: Feature disabled.
* **6008 `INVALID_SECRET_FORMAT`**: Malformed secret.
* **6009 `CONFIG_CONFLICT`**: Conflicting configs.
* **6010 `VERSION_MISMATCH`**: Version incompatibility.

---

## 7000–7999: Network / Timeout Errors

Connectivity and timeout related errors.

* **7001 `NETWORK_UNREACHABLE`**: Network or DNS failure.
* **7002 `REQUEST_TIMEOUT`**: Request timed out.
* **7003 `HOST_UNAVAILABLE`**: Host is down.
* **7004 `SSL_HANDSHAKE_FAILED`**: TLS handshake failed.
* **7005 `DNS_RESOLUTION_FAILED`**: Domain resolution failed.
* **7006 `PROXY_ERROR`**: Proxy failure.
* **7007 `CONNECTION_RESET`**: Connection dropped.
* **7008 `TOO_MANY_REDIRECTS`**: Redirect loop.
* **7009 `FIREWALL_BLOCKED`**: Request blocked by firewall.
* **7010 `BANDWIDTH_LIMIT_EXCEEDED`**: Data transfer limit exceeded.

---

## 8000–8999: Internal Server Errors

Unexpected internal exceptions and crashes.

* **8001 `INTERNAL_SERVER_ERROR`**: Unknown failure.
* **8002 `NULL_POINTER_EXCEPTION`**: Null value accessed.
* **8003 `ILLEGAL_STATE`**: Invalid operation state.
* **8004 `TYPE_CAST_EXCEPTION`**: Serialization or casting error.
* **8005 `THREAD_INTERRUPTED`**: Thread interrupted.
* **8006 `STACK_OVERFLOW`**: Stack overflow error.
* **8007 `HEAP_MEMORY_EXCEEDED`**: Out of JVM memory.
* **8008 `RESOURCE_LEAK`**: Unreleased resource.
* **8009 `UNSUPPORTED_OPERATION`**: Operation not implemented.
* **8010 `SCHEDULER_ERROR`**: Scheduled task failure.

---

## 9000–9999: Unknown / Generic Errors

Miscellaneous or unclassified errors.

* **9001 `UNKNOWN_ERROR`**: Unrecognized error type.
* **9002 `UNHANDLED_EXCEPTION`**: No catch block handled error.
* **9003 `GENERIC_FAILURE`**: Non-specific failure.
* **9004 `UNDEFINED_BEHAVIOR`**: Invalid code logic state.
* **9005 `RETRY_LIMIT_EXCEEDED`**: Retry attempts exhausted.
* **9006 `MODULE_INIT_FAILED`**: Module initialization failed.
* **9007 `TEMPORARY_ERROR`**: Transient error, might self-resolve.
* **9008 `FEATURE_NOT_IMPLEMENTED`**: Endpoint not implemented.
* **9009 `LOGGING_FAILURE`**: Logging subsystem failure.
* **9010 `SHUTDOWN_IN_PROGRESS`**: System is shutting down.

---

### Notes

* Use the error code ranges to quickly identify the error domain.
* Each error includes a unique numeric code and a descriptive constant name for programmatic reference.
* Error descriptions provide clarity for developers and support staff to diagnose and resolve issues effectively.