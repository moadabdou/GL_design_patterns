# Memento

## What It Is
Memento is a behavioral pattern that captures and restores an object's internal state without exposing its details.

It is commonly used for undo functionality.

## When to Use It
- You need undo/rollback behavior.
- You want to save snapshots of state.
- You want to preserve encapsulation while storing state.

## Java Example
```java
class EditorMemento {
    private final String state;

    EditorMemento(String state) {
        this.state = state;
    }

    String getState() {
        return state;
    }
}

class Editor {
    private String text;
}
```

## Typical Use Cases
- Undo/redo
- Checkpointing
- Game save states
- Form drafts

## Benefits
- Preserves encapsulation
- Makes rollback easy
- Separates state history from business logic

## Tradeoffs
- Can use a lot of memory
- Snapshot management can be expensive
