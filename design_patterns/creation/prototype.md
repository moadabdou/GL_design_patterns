# Prototype

## What It Is
Prototype is a creational design pattern that creates new objects by copying an existing object instead of building one from scratch.

It is useful when object creation is expensive or complicated.

## When to Use It
- Creating an object is costly.
- You need many objects that start from the same base state.
- You want to avoid repeated initialization logic.

## Java Example
```java
class Document implements Cloneable {
    private String title;
    private String content;

    public Document(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Document clone() {
        try {
            return (Document) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

// Usage:
// Document original = new Document("Guide", "Base content");
// Document copy = original.clone();
// copy.setTitle("Guide Copy");
```

## Typical Use Cases
- Cloning documents, shapes, or game objects
- Creating default templates for configuration objects
- Duplicating cached objects
- Copying complex records with shared structure

## Benefits
- Faster than full re-creation in many cases
- Hides complex construction logic
- Lets you create objects at runtime from existing instances

## Tradeoffs
- Deep copy handling can be tricky
- Cloning can be error-prone if objects contain references
- Requires careful design for mutable state
