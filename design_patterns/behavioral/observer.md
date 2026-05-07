# Observer

## What It Is
Observer is a behavioral pattern where one object notifies a list of dependent objects when its state changes.

It creates a publish-subscribe style relationship.

## When to Use It
- Many objects depend on one object's state.
- You want automatic notifications.
- You want a loosely coupled event system.

## Java Example
```java
interface Observer {
    void update(String message);
}

class Subject {
    private final java.util.List<Observer> observers = new java.util.ArrayList<>();

    void addObserver(Observer observer) {
        observers.add(observer);
    }
}
```

## Typical Use Cases
- Event systems
- UI updates
- News feeds
- Model-view synchronization

## Benefits
- Loose coupling
- Easy to add new observers
- Useful for event-driven designs

## Tradeoffs
- Notification chains can be hard to debug
- Too many updates can hurt performance
