# Builder

## What It Is
Builder is a creational design pattern that separates the construction of a complex object from its representation.

It lets you build the same object step by step, while keeping the construction process readable and flexible.

## When to Use It
- An object has many optional parameters.
- You want to avoid constructors with long parameter lists.
- You need different representations of the same construction process.

## Java Example
```java
class Computer {
    private final String cpu;
    private final String ram;
    private final String storage;

    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
    }

    public static class Builder {
        private String cpu;
        private String ram;
        private String storage;

        public Builder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder ram(String ram) {
            this.ram = ram;
            return this;
        }

        public Builder storage(String storage) {
            this.storage = storage;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}

// Usage:
// Computer computer = new Computer.Builder()
//         .cpu("Intel i7")
//         .ram("16 GB")
//         .storage("1 TB SSD")
//         .build();
```

## Typical Use Cases
- Building immutable objects
- Creating request objects or configuration objects
- Constructing complex domain models
- Generating test data with optional fields

## Benefits
- Improves readability
- Makes object creation safer
- Handles optional fields cleanly

## Tradeoffs
- Adds extra classes or nested builders
- Can be unnecessary for very simple objects
