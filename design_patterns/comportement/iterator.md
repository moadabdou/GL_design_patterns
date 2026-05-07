# Iterator

## What It Is
Iterator is a behavioral pattern that lets you traverse a collection without exposing its internal representation.

It provides a uniform way to access elements one by one.

## When to Use It
- You need to access elements sequentially.
- You want to hide collection internals.
- You want multiple traversal strategies.

## Java Example
```java
interface Iterator<T> {
    boolean hasNext();
    T next();
}

class NameCollection {
    private final String[] names = {"A", "B", "C"};
}
```

## Typical Use Cases
- Custom collections
- Tree traversals
- Database cursors
- Lazy sequence access

## Benefits
- Separates traversal from collection
- Supports different iteration strategies
- Simplifies client code

## Tradeoffs
- Can be overkill for simple collections
- May add extra classes and boilerplate
