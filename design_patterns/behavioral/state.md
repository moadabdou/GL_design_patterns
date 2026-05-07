# State

## What It Is
State is a behavioral pattern that lets an object change its behavior when its internal state changes.

It appears as if the object changes its class.

## When to Use It
- An object has behavior that depends on state.
- You want to avoid large if-else or switch blocks.
- State transitions are important in the domain.

## Java Example
```java
interface State {
    void handle();
}

class DraftState implements State {
    public void handle() {
        System.out.println("Handling draft");
    }
}

class Order {
    private State state;
}
```

## Typical Use Cases
- Order lifecycles
- Workflow engines
- Vending machines
- Document publishing states

## Benefits
- Clean state-specific behavior
- Removes complex conditional logic
- Makes transitions explicit

## Tradeoffs
- Can add many state classes
- State transitions must be carefully managed
