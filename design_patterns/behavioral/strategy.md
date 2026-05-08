# Strategy

## What It Is

Strategy is a behavioral pattern that defines a family of algorithms, encapsulates each one, and makes them interchangeable.

It lets the algorithm vary independently from the client that uses it. Instead of hardcoding logic into a class, you extract that logic into separate strategy objects that implement a common interface.

The core idea: **defer the choice of algorithm to runtime** rather than baking it into your class hierarchy.

## When to Use It

- You need different ways to perform the same task.
- You want to switch algorithms at runtime without modifying client code.
- You want to avoid large conditional blocks (if-else chains).
- You have multiple implementations of the same concept and want them to be interchangeable.
- The algorithm is likely to change or new algorithms will be added frequently.

## Problem It Solves

Without Strategy, you might write code like this (anti-pattern):

```java
class DocumentExporter {
    public void export(Document doc, String format) {
        if (format.equals("PDF")) {
            // 50 lines of PDF logic
        } else if (format.equals("HTML")) {
            // 50 lines of HTML logic
        } else if (format.equals("Markdown")) {
            // 50 lines of Markdown logic
        }
    }
}
```

**Problems:**
- The class becomes large and hard to maintain.
- Adding a new format requires modifying this class (violates Open/Closed Principle).
- Testing is difficult because you're testing multiple algorithms in one class.

## Solution: Strategy Pattern

Extract each algorithm into its own class that implements a common interface:

```java
// Strategy interface
interface ExportStrategy {
    void export(Document doc, String filename);
}

// Concrete strategies
class PDFExportStrategy implements ExportStrategy {
    @Override
    public void export(Document doc, String filename) {
        // PDF-specific logic
        System.out.println("Exporting to PDF: " + filename);
    }
}

class HTMLExportStrategy implements ExportStrategy {
    @Override
    public void export(Document doc, String filename) {
        // HTML-specific logic
        System.out.println("Exporting to HTML: " + filename);
    }
}

class MarkdownExportStrategy implements ExportStrategy {
    @Override
    public void export(Document doc, String filename) {
        // Markdown-specific logic
        System.out.println("Exporting to Markdown: " + filename);
    }
}

// Context class - uses strategies
class DocumentExporter {
    private ExportStrategy strategy;

    public DocumentExporter(ExportStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ExportStrategy strategy) {
        this.strategy = strategy;
    }

    public void export(Document doc, String filename) {
        strategy.export(doc, filename);
    }
}
```

## Complete Example Usage

```java
public class Main {
    public static void main(String[] args) {
        Document doc = new Document("My Document");

        // Use PDF strategy
        DocumentExporter exporter = new DocumentExporter(new PDFExportStrategy());
        exporter.export(doc, "output.pdf");

        // Switch to HTML at runtime
        exporter.setStrategy(new HTMLExportStrategy());
        exporter.export(doc, "output.html");

        // Switch to Markdown
        exporter.setStrategy(new MarkdownExportStrategy());
        exporter.export(doc, "output.md");
    }
}
```

## Real-World Analogy

Think of a **payment system**:
- Different customers have different payment methods: credit cards, PayPal, Bitcoin, etc.
- Each method has its own "algorithm" (validation, processing, confirmation).
- But from the shopping cart's perspective, it just needs to call `pay()` on whatever payment method was selected.
- You can switch methods at checkout without the cart code needing to change.

## Key Components

1. **Strategy Interface** - Defines the contract all strategies must follow.
2. **Concrete Strategies** - Implement the interface with different algorithms.
3. **Context** - Uses a strategy object and can switch between them.
4. **Client** - Chooses which strategy to pass to the context.

## Typical Use Cases

- **Payment methods** - Credit card, PayPal, cryptocurrency, bank transfer.
- **Sorting algorithms** - QuickSort, MergeSort, BubbleSort.
- **Compression algorithms** - ZIP, GZIP, RAR.
- **Pricing or discount rules** - Loyalty discount, seasonal discount, bulk discount.
- **Export formats** - PDF, HTML, Excel, JSON.
- **Authentication methods** - OAuth, JWT, LDAP, SAML.
- **Routing algorithms** - GPS navigation (fastest route, shortest route, scenic route).

## Benefits

- **Open/Closed Principle** - Open for extension (add new strategies), closed for modification (context class doesn't change).
- **Easy to swap algorithms** - Change behavior at runtime without conditional logic.
- **Testability** - Each strategy can be tested in isolation.
- **Reduces conditional logic** - No more massive if-else chains.
- **Single Responsibility** - Each strategy class handles one algorithm.
- **Easier to understand** - Code is more readable and maintainable.

## Tradeoffs

- **More classes** - Each strategy becomes a new class, so you have more files to manage.
- **Overhead for simple cases** - If you only have one or two implementations, the pattern might be overkill.
- **Client complexity** - Clients must know which strategy to use and how to instantiate it.
- **Runtime cost** - Polymorphic calls are slightly slower than direct method calls (negligible in most cases).

## Comparison with Other Patterns

| Pattern | Focus | When to Use |
|---------|-------|------------|
| **Strategy** | Encapsulate interchangeable algorithms | Multiple ways to do the same thing |
| **State** | Alter object behavior based on internal state | Object changes behavior based on its own state |
| **Command** | Encapsulate requests as objects | Undo/redo, queuing, logging operations |
| **Template Method** | Define algorithm skeleton, let subclasses fill in details | Fixed algorithm with variable steps |

## Common Pitfalls

1. **Over-engineering** - Don't use Strategy for something with only one implementation.
2. **Unclear when to switch** - Make it obvious to the client when/why to switch strategies.
3. **Shared state problems** - Be careful if strategies share mutable state.
4. **Not documenting strategy selection** - Document which strategy to use in which scenario.

## Implementation Guidelines

- Use an **interface** to define the strategy contract (not an abstract class, unless you need shared code).
- Keep strategies **stateless** when possible (make them singleton if appropriate).
- Consider a **Factory** or **Strategy registry** to manage strategy instantiation.
- Use **dependency injection** to pass strategies to the context.
- Document which strategy is the **default** and why others exist.
