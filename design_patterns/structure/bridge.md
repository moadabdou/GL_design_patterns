# Bridge

## What It Is
Bridge is a structural design pattern that separates an abstraction from its implementation so the two can vary independently.

It is useful when you want to avoid a large number of subclasses created by combining features and platforms.

## When to Use It
- You want to split high-level logic from low-level implementation.
- You need to support multiple implementations without subclass explosion.
- You want to switch implementations at runtime.

## Java Example
```java
interface Device {
    void turnOn();
    void turnOff();
}

class TV implements Device {
    public void turnOn() { System.out.println("TV on"); }
    public void turnOff() { System.out.println("TV off"); }
}

abstract class RemoteControl {
    protected final Device device;

    protected RemoteControl(Device device) {
        this.device = device;
    }

    public void powerOn() {
        device.turnOn();
    }
}
```

## Typical Use Cases
- UI toolkits and platform-specific rendering
- Remote controls and devices
- Payment systems with multiple gateways
- Logging abstractions with different output targets

## Benefits
- Reduces class explosion
- Improves extensibility
- Decouples abstraction from implementation

## Tradeoffs
- Adds complexity up front
- Requires planning the abstraction boundary well
