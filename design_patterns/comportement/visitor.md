# Visitor

## What It Is
Visitor is a behavioral pattern that lets you add new operations to object structures without changing the classes of the elements.

It is useful when you have a stable structure but need many different operations on it.

## When to Use It
- You want to add operations without modifying element classes.
- You have a stable object structure.
- You need several unrelated operations across the same object graph.

## Java Example
```java
interface Visitor {
    void visit(Book book);
}

class Book {
    void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
```

## Typical Use Cases
- AST processing
- Report generation
- Export/import logic
- File system operations

## Benefits
- Easy to add new operations
- Separates operations from data structures
- Can simplify complex traversals

## Tradeoffs
- Harder to add new element types
- Can break encapsulation if overused
