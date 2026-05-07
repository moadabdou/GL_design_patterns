# Facade

## What It Is
Facade is a structural design pattern that provides a simplified interface to a larger, more complex subsystem.

It helps hide internal complexity and gives the client an easier way to use the system.

## When to Use It
- You want to simplify access to a complicated subsystem.
- You want to reduce dependencies between client code and subsystem classes.
- You want a cleaner API for a group of operations.

## Java Example
```java
class CPU {
    void start() { System.out.println("CPU started"); }
}

class Memory {
    void load() { System.out.println("Memory loaded"); }
}

class ComputerFacade {
    private final CPU cpu = new CPU();
    private final Memory memory = new Memory();

    public void startComputer() {
        cpu.start();
        memory.load();
    }
}
```

## Typical Use Cases
- Simplifying complex libraries
- Providing a single entry point to a subsystem
- Hiding setup and orchestration logic
- Creating service wrappers

## Benefits
- Makes APIs easier to use
- Reduces coupling
- Improves readability

## Tradeoffs
- Can become a “god object” if it does too much
- May hide useful subsystem functionality
