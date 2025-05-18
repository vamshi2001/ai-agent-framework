
# AI-Agent Framework Cache System

This document explains the caching flow architecture used in the AI-Agent Framework. It is designed for performance and modularity — providing in-memory cache access for fast reads/writes and extensible sync capabilities with external cache systems like Redis or NoSQL databases.

---

## 🧠 Overview

The cache system is designed with **two major responsibilities**:

1. **In-memory read/write operations** — fast and efficient access for business logic.
2. **Syncing to actual cache sources (e.g., Redis)** — controlled via custom implementations.

---

## 🔌 Interface Definitions

### `Cache<K, V>`

Used by business logic to perform basic in-memory cache operations.

```java
public interface Cache<K, V> {
    boolean save(K key, V value);
    V get(K key);
    boolean put(K key, V value);
    boolean delete(K key);
}
````

### `CacheOperations<K, V>`

Used internally for managing sync operations with external systems.

```java
public interface CacheOperations<K, V> {
    boolean source();                      // Load data from external source into RAM
    boolean sink(K key);                   // Write updated key-value back to source
    boolean refresh();                     // Refresh all or specific keys
    boolean clear();                       // Clear in-memory cache
    long getLastRefreshTime();            // Get last successful refresh timestamp
    void notifyCacheHandler(K key);        // Optional hook to handle key updates
}
```

---

## 🏗 Class Hierarchy & Responsibilities

```plaintext
       +----------------------+
       | Cache<K,V>          |
       | CacheOperations<K,V>|
       +----------------------+
                ▲
                |
      +--------------------------+
      | InMemoryCache<K, V>      |  <-- Abstract class
      +--------------------------+
      | - Stores data in RAM     |
      | - Implements Cache<K,V>  |
      | - Defines map store      |
      +--------------------------+
                ▲
                |
      +------------------------------+
      | AbstractCacheOperations<K,V> |  <-- Abstract class
      +------------------------------+
      | - Implements refresh logic   |
      | - Tracks last refresh time   |
      | - Registers cache instance   |
      +------------------------------+
                ▲
                |
      +-----------------------------+
      | YourCustomCacheHandler<K,V> |  <-- User-defined, e.g. RedisCacheHandler
      +-----------------------------+
      | - Implements source/sink    |
      +-----------------------------+

      (or)
      +----------------------------+
      | SimpleCacheHandler<K, V>   |  <-- Default no-op cache handler
      +----------------------------+
```

---

## 🚀 How It Works

1. **Business Logic Layer** only interacts with `Cache<K, V>` for read/write/delete.
2. All cache data is first saved in **RAM** via `InMemoryCache`.
3. Optionally, `source()` can fetch data from an external source (e.g., Redis).
4. Optionally, `sink(K key)` pushes a modified key-value back to the external system.
5. `AbstractCacheOperations` handles periodic or triggered **refreshing** of all cache instances, using timestamps.

---

## 🧪 Default Implementation

Use `SimpleCacheHandler<K, V>` for testing or cases where no external cache source is needed.

```java
public class SimpleCacheHandler<K, V> extends AbstractCacheOperations<K, V> {
    @Override
    public boolean source() {
        return true; // No-op
    }

    @Override
    public boolean sink(K key) {
        return true; // No-op
    }
}
```

---

## 🛠 How to Extend (Custom Cache Handler)

To integrate with Redis or another external system:

1. Extend `AbstractCacheOperations<K, V>`.
2. Implement:

   * `boolean source()` — fetch and populate internal map.
   * `boolean sink(K key)` — push data for a specific key.
3. Register it in your environment.

```java
public class RedisCacheHandler<K, V> extends AbstractCacheOperations<K, V> {
    @Override
    public boolean source() {
        // fetch all data from Redis and put into map
    }

    @Override
    public boolean sink(K key) {
        // sync updated key to Redis
    }
}
```

---

## ⏱ Refresh System

* Each cache keeps track of the **last refresh timestamp**.
* `AbstractCacheOperations` registers itself with a cache refresher module.
* Refresher can trigger `.refresh()` based on stale data detection or a time interval.