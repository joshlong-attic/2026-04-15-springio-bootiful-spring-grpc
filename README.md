# Bootiful Spring gRPC

Spring gRPC — Spring I/O 2026 (co-hosted by Dave Syer and Josh Long)

The Spring gRPC project provides Spring-friendly APIs and abstractions for developing gRPC applications. It includes a core library that integrates gRPC with Spring's dependency injection model and a Spring Boot starter that provides autoconfiguration and configuration properties to make it easy to get started with gRPC in Spring Boot applications.

Documentation: https://docs.spring.io/spring-grpc/reference/

Source (GitHub): https://github.com/spring-projects/spring-grpc

| Spring gRPC version | Release date | Compatible Spring Boot |
|---|---:|---|
| `1.1.0-M1` (milestone) | Mar 2026 (milestone) | Targets Spring Boot `4.1.0` (milestone)
| `1.0.2` | Jan 29, 2026 | Spring Boot `4.0.x` or `4.1.x`
| `1.0.0` | Dec 4, 2025 | Spring Boot `4.0.x`
| `0.12.0` | Oct 24, 2025 | Spring Boot `3.5.x`



## Incantations


```bash
grpcurl -plaintext localhost:9090 grpc.health.v1.Health/Check      
```

```bash
grpcurl -plaintext  -d '{ "name": "Tommy-san"}' localhost:9090 Greetings.hello
```