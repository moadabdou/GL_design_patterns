# Decorator

## What It Is
Decorator is a structural design pattern that lets you add new behavior to an object dynamically by wrapping it.

It is useful when you want to extend behavior without changing the original class.

## When to Use It
- You want to add features at runtime.
- You want to avoid a large number of subclasses.
- You want to keep responsibilities separate and composable.

## Java Example
```java
interface Coffee {
    String getDescription();
}

class SimpleCoffee implements Coffee {
    public String getDescription() {
        return "Simple coffee";
    }
}

class MilkDecorator implements Coffee {
    private final Coffee coffee;

    MilkDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String getDescription() {
        return coffee.getDescription() + ", milk";
    }
}
```

## Typical Use Cases
- Adding features to I/O streams
- Adding formatting or logging behavior
- Extending UI components
- Building configurable service pipelines

## Benefits
- Flexible and reusable
- Supports composition over inheritance
- Keeps responsibilities separated

## Tradeoffs
- Can create many small classes
- Harder to debug when many wrappers are stacked
