# 📘 AI Agent Framework - Environment Processing Architecture

## 🧠 Overview

This framework provides a **pluggable AI agent processing pipeline** where users can define their own `Environment` implementations to process tasks, invoke agents, and manage conversation state. The default implementation is provided via `StatelessEnvironmentHandler`, but developers can **override** it by setting:

```properties
env.handler.default=false
```

and defining their own `Environment` bean.

---

## 🔧 Components and Their Responsibilities

### 1. **Environment Interface**

Defines the `process()` method, which handles agent selection and task execution logic.

### 2. **EnvironmentLoader**

Base class that provides access to application context and shared utilities. It is extended by `StatelessEnvironmentHandler`.

### 3. **StatelessEnvironmentHandler**

**Default implementation** of the `Environment` logic. It:

* Retrieves agent pool from Spring context.
* Selects a task from the goal.
* Matches it with an agent.
* Executes agent logic via `AgentDefinition.invokeAgent`.
* Updates task and agent histories.
* Loops until no further tasks are pending.

### 4. **AgentPool**

Registry of available agents that can match specific tasks based on predefined rules.

### 5. **AgentDefinition**

Encapsulates logic for an individual AI agent, including matching rules and execution.

### 6. **Goal / Task / Action / State**

Data models used to structure user goals, track actions taken, and define what task each agent will perform.

---

## 🏗️ Architecture Diagram

```
              ┌─────────────────────┐
              │ User Input (Text)   │
              └─────────┬───────────┘
                        │
                        ▼
            ┌───────────────────────────┐
            │ StatelessEnvironmentHandler│
            │ (implements process)       │
            └─────────┬─────────────────┘
                      │
                      ▼
            ┌─────────────────────┐
            │ Retrieve AgentPool  │◄─────────────┐
            └─────────┬───────────┘              │
                      ▼                          │
          ┌──────────────────────────────┐       │
          │ Match Task to AgentDefinition│       │
          └─────────┬────────────────────┘       │
                    ▼                            │
        ┌────────────────────────────┐           │
        │  AgentDefinition.invoke()  │           │
        └─────────┬──────────────────┘           │
                  ▼                              │
         ┌──────────────────────────────┐        │
         │ Update State and History     │        │
         └─────────┬────────────────────┘        │
                   ▼                             │
         ┌──────────────────────────────┐        │
         │ Select Next Task from Goal   │────────┘
         └─────────┬────────────────────┘
                   ▼
            Loop Until No Tasks Left

```

---

## 🔄 Extending the Framework

To provide a **custom environment processing logic**:

1. Implement your own class extending `EnvironmentLoader`.
2. Override `process(EnvironmentState, String, InputOutputHandler)` method.
3. Add your class as a Spring component.
4. Set in `application.properties`:

```properties
env.handler.default=false
```

---

## ✅ Benefits

* Modular and plug-and-play architecture.
* Clear separation of concerns.
* Supports customization without touching core logic.
* Built-in support for conversation history and agent chaining.
