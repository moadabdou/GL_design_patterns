# Factory Method

## What It Is
Factory Method is a creational design pattern that defines an interface for creating objects, but lets subclasses decide which class to instantiate.

It moves object creation into a separate method so the client code depends on abstractions instead of concrete classes.

## When to Use It
- A class cannot anticipate the class of objects it must create.
- You want subclasses to control object creation.
- You want to avoid tight coupling to concrete implementations.

## Java Example
```java
interface Transport {
    void deliver();
}

class Truck implements Transport {
    public void deliver() {
        System.out.println("Deliver by road");
    }
}

class Ship implements Transport {
    public void deliver() {
        System.out.println("Deliver by sea");
    }
}

abstract class Logistics {
    public abstract Transport createTransport();

    public void planDelivery() {
        Transport transport = createTransport();
        transport.deliver();
    }
}

class RoadLogistics extends Logistics {
    public Transport createTransport() {
        return new Truck();
    }
}

class SeaLogistics extends Logistics {
    public Transport createTransport() {
        return new Ship();
    }
}
```

## Typical Use Cases
- Frameworks that let subclasses provide concrete behavior
- Logging systems with pluggable output handlers
- UI toolkits that create platform-specific controls
- Payment processing integrations

## Benefits
- Reduces dependency on concrete classes
- Makes the code easier to extend
- Follows the open/closed principle better than direct instantiation

## Tradeoffs
- Can introduce more subclasses
- May feel heavy for simple creation logic
