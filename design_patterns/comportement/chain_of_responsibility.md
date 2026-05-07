# Chain of Responsibility

## What It Is
Chain of Responsibility is a behavioral pattern that passes a request through a chain of handlers until one of them processes it.

It helps avoid coupling the sender of a request to a specific receiver.

## When to Use It
- More than one object may handle a request.
- You want to decouple sender and receiver.
- You want to build flexible processing pipelines.

## Java Example
```java
abstract class Handler {
    protected Handler next;

    public Handler setNext(Handler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(String request);
}

class AuthHandler extends Handler {
    public void handle(String request) {
        if ("auth".equals(request)) {
            System.out.println("Handled by auth");
        } else if (next != null) {
            next.handle(request);
        }
    }
}
```

## Typical Use Cases
- Validation pipelines
- Logging chains
- Middleware and filters
- Request authorization

## Benefits
- Flexible handler ordering
- Low coupling
- Easy to extend with new handlers

## Tradeoffs
- A request may go through many handlers
- Can be hard to trace the final handler
