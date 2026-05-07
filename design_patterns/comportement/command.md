# Command

## What It Is
Command is a behavioral pattern that turns a request into an object.

This lets you parameterize methods, queue actions, and support undo operations.

## When to Use It
- You want to store or queue operations.
- You need undo/redo behavior.
- You want to decouple invoker and receiver.

## Java Example
```java
interface Command {
    void execute();
}

class Light {
    void on() {
        System.out.println("Light on");
    }
}

class LightOnCommand implements Command {
    private final Light light;

    LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.on();
    }
}
```

## Typical Use Cases
- Button actions
- Macro recording
- Job queues
- Undo systems

## Benefits
- Decouples sender and receiver
- Supports undo and logging
- Makes commands reusable

## Tradeoffs
- More classes are needed
- Can add indirection for simple actions
