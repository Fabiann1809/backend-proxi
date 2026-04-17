# AI Proxy Platform

Unified gateway API that acts as a proxy for multiple AI provider APIs (OpenAI, Anthropic, Google Gemini, etc.), exposing a single normalized interface to clients.

## Tech Stack

- **Java 21**
- **Spring Boot 3.4.x**
- **Maven**

## Getting Started

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Project Structure

```
src/main/java/com/fabian/aiproxy/
├── AiProxyApplication.java   # Entry point
├── controller/               # REST controllers
├── service/                  # Business logic
│   └── proxy/                # Provider-specific proxy services
├── model/                    # Domain entities
│   └── enums/                # Enumerations
├── dto/
│   ├── request/              # Incoming request DTOs
│   └── response/             # Outgoing response DTOs
├── config/                   # Spring configuration beans
└── scheduler/                # Scheduled tasks
```

## Configuration

Edit `src/main/resources/application.properties` to adjust server port, logging, and provider API keys.
