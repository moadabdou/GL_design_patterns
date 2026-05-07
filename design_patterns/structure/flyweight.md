# Flyweight

## What It Is
Flyweight is a structural design pattern that reduces memory usage by sharing common object state between multiple objects.

It is useful when you need to create a large number of similar objects.

## When to Use It
- You have many objects with shared intrinsic state.
- You want to save memory.
- Object creation is expensive at scale.

## Java Example
```java
class TreeType {
    private final String name;

    TreeType(String name) {
        this.name = name;
    }

    void draw(int x, int y) {
        System.out.println("Drawing " + name + " at " + x + "," + y);
    }
}

class TreeFactory {
    private final java.util.Map<String, TreeType> cache = new java.util.HashMap<>();

    TreeType getTreeType(String name) {
        return cache.computeIfAbsent(name, TreeType::new);
    }
}
```

## Typical Use Cases
- Text editors
- Game rendering
- Map markers
- Large grids or repeated UI elements

## Benefits
- Reduces memory consumption
- Reuses shared state efficiently
- Improves performance for large object counts

## Tradeoffs
- Makes code more complex
- Requires careful separation of shared and unique state
