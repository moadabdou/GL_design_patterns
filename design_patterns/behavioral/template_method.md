# Template Method

## What It Is
Template Method is a behavioral pattern that defines the skeleton of an algorithm in a base class and lets subclasses fill in specific steps.

It keeps the overall flow fixed while allowing parts of the behavior to change.

## When to Use It
- You have a common algorithm structure.
- Some steps should vary between subclasses.
- You want to avoid duplicating the overall process.

## Java Example
```java
abstract class DataParser {
    public final void parse() {
        open();
        read();
        close();
    }

    protected abstract void read();

    private void open() {
        System.out.println("Open file");
    }

    private void close() {
        System.out.println("Close file");
    }
}
```

## Typical Use Cases
- File parsers
- Data import pipelines
- Framework hooks
- Reusable workflows

## Benefits
- Reuses common logic
- Enforces algorithm structure
- Lets subclasses customize steps

## Tradeoffs
- Uses inheritance, which can be rigid
- Harder to change algorithm structure later
