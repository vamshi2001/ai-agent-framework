# 📘 Error Segment Categories

This document defines standardized error **segments** used throughout our backend system to classify and handle errors more effectively. These segments are used across logs, exception handling, observability dashboards, and alerting mechanisms.

By tagging logs and exceptions with specific segments, we ensure better visibility, easier debugging, and more actionable monitoring.

---

## 🧩 Segments & Their Meaning

Below are the key segments and what they represent:

### 🤖 `chatbot`

Handles user interactions in chat-based interfaces — includes input parsing, flow handling, and bot communication errors.

### 🧠 `ai`

Covers LLM (Large Language Model) tasks such as prompt processing, model inference, and AI reasoning workflows.

### 📬 `kafka`

Related to event streaming and message brokers. Use for issues with publishing, consuming, or topic configuration in Kafka.

### ✉️ `mail`

Email-related operations, including failures in SMTP delivery, configuration issues, or bounce handling.

### 🔐 `auth`

Authentication and authorization logic — includes login, token validation, session checks, and permissions.

### 🗄️ `db`

Database-related actions across SQL or NoSQL systems. Covers query execution, schema issues, and data access.

### 🔌 `api`

Errors from third-party or internal API interactions — including failed requests, timeouts, or malformed responses.

### ⚙️ `config`

Issues arising from environment variables, configuration files, or dynamic settings loading.

### 🚀 `cache`

Failures in caching layers such as Redis, Memcached, or custom in-memory solutions.

### 🔍 `search`

Search infrastructure problems — including Elasticsearch, OpenSearch, or vector-based search backends.

### 📦 `storage`

Covers file uploads, downloads, and object storage systems like Amazon S3 or Google Cloud Storage.

### 📈 `metrics`

Relates to application observability — metric reporting, Prometheus errors, or missing Grafana dashboards.

### 🔄 `webhook`

Inbound or outbound webhook failures, such as delivery errors, authentication problems, or payload mismatches.

### ⏰ `scheduler`

Scheduled job issues, including cron failures, missed triggers, or job overlaps.

### 💳 `payment`

Payment gateway communication issues — transaction failures, webhook rejections, or config mismatches.

### 📢 `notif`

Notification delivery failures, including SMS, push notifications, in-app alerts, or multi-channel dispatching.

### 🧬 `llm`

Specific to large language model processing — API errors, generation faults, or output parsing problems.

### 🧮 `vector`

Vector DB-related issues, such as Pinecone, FAISS, Qdrant indexing, similarity search, or insert/query failures.

### 🤖 `rasa`

Covers Rasa NLU/Core logic — intent detection, dialogue management, and response generation.

### 🗣️ `nlu`

General natural language understanding failures — misinterpreted user intents, entity recognition, etc.

### 🏭 `pipeline`

Data processing or machine learning pipeline failures — includes feature transformation, staging, or batch processing.

### 🖥️ `frontend`

Issues in communication between the backend and front-end (web/mobile), including REST errors and JSON mismatches.

### 🛡️ `middleware`

Middleware stack errors — auth filters, interceptors, rate limiters, and global request/response handlers.

### 🪪 `session`

Problems storing or retrieving session data — usually in Redis or local memory stores.

### 🌍 `cdn`

Static asset delivery failures through content delivery networks (e.g., images, JS/CSS not loading).

### ⬆️ `upload`

Covers file/media upload errors, format validation, size restrictions, or multipart/form-data parsing.

---

## 🧠 How to Use

When logging an error or throwing an exception, include the appropriate segment to pinpoint where the issue occurred:

```java
throw new ApiHubException("1001-db", "Failed to fetch user from DB");
```

Or in logs:

```
[ERROR] [ai] LLM API failed with timeout for input: "User query..."
```

---

## ✅ Benefits

* 🔍 **Faster Root Cause Analysis**
* 📊 **Segmented Monitoring Dashboards**
* 🚨 **Smart Alert Routing**
* 🧭 **Improved DevOps Workflow**
