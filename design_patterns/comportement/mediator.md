# Mediator

## What It Is
Mediator is a behavioral pattern that centralizes communication between objects.

Instead of objects talking directly to each other, they talk through a mediator.

## When to Use It
- Many objects interact in complex ways.
- You want to reduce direct dependencies.
- You want to centralize coordination logic.

## Java Example
```java
interface ChatMediator {
    void sendMessage(String message, User user);
}

class User {
    private final String name;
    private final ChatMediator mediator;

    User(String name, ChatMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }
}
```

## Typical Use Cases
- Chat rooms
- GUI dialog coordination
- Air traffic control logic
- Workflow orchestration

## Benefits
- Reduces many-to-many dependencies
- Centralizes complex interaction rules
- Makes components easier to reuse

## Tradeoffs
- Mediator can become large and complex
- Central point may become a bottleneck
