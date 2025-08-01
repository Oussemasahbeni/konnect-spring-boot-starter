# Konnect Spring Boot Starter

A Spring Boot starter to easily integrate Konnect payment services into your Spring Boot applications.

## Features

- Simple configuration for Konnect API integration
- Payment initialization and management
- Exception handling for payment workflows
- Extensible and customizable

## Requirements

- Java 21 or higher
- Spring Boot 3.x

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.oussemasahbeni</groupId>
    <artifactId>konnect-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Configuration

Add the following properties to your `application.properties` or `application.yml`:

```properties
konnect.api-key=YOUR_KONNECT_API_KEY
konnect.base-url=https://api.konnect.network
# Add other configuration as needed
```

## Usage

Inject the `KonnectClient` into your service:

```java
import com.oussemasahbeni.konnect.client.KonnectClient;

@Service
public class PaymentService {
    private final KonnectClient konnectClient;

    public PaymentService(KonnectClient konnectClient) {
        this.konnectClient = konnectClient;
    }

    // Use konnectClient methods to interact with Konnect API
}
```

## Development

- Clone the repository
- Build with Maven: `./mvnw clean install`

## License

MIT

