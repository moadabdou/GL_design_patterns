# Project Idea: Document / Page Builder with Export & Formatting

## Core Concept
A conceptual but functional Document Builder application designed specifically to cleanly incorporate exactly 10 Design Patterns.

## Why this project?
- It naturally mixes **Creational** (how objects are made), **Structural** (how they’re composed), and **Behavioral** (how they interact).
- Each pattern solves a real, recognizable minor “pain point” commonly found in software design, rather than feeling arbitrarily forced.

---

## Pattern Mapping & Justification

| # | Pattern | Category | Role in the Project |
|---|---------|----------|----------------------|
| 1 | **Builder** | Creational | Build complex `Document` objects (title, paragraphs, images, styles) step by step, avoiding telescoping constructors. |
| 2 | **Factory Method** | Creational | Create different types of `Element` (TextElement, ImageElement, TableElement). |
| 3 | **Singleton** | Creational | Guarantee one global `Configuration` or `Logger` instance for the entire system. |
| 4 | **Adapter** | Structural | Adapt a 3rd-party PDF library to our `Exporter` interface. |
| 5 | **Decorator** | Structural | Add formatting (bold, italic, color) to document elements dynamically without subclassing. |
| 6 | **Facade** | Structural | Simplify document saving/exporting into one clean `DocumentExporterFacade`. |
| 7 | **Strategy** | Behavioral | Enable different interchangeable export formats: `PDFExportStrategy`, `HTMLExportStrategy`, `MarkdownStrategy`. |
| 8 | **Observer** | Behavioral | Notify UI or logger when the document changes or a slow export finishes. |
| 9 | **Command** | Behavioral | Implement Undo/Redo operations (add element, delete element, change style). |
| 10 | **Iterator** | Behavioral | Iterate over document elements without exposing the underlying internal data structures. |

---

## Why this works for a teacher demo
- **Builder** shows how to avoid telescoping constructors cleanly.
- **Decorator + Strategy** together beautifully demonstrate the Open/Closed Principle.
- **Command** shows robust undo/redo (impressive functionality for a small codebase).
- **Iterator** is simple but enforces good encapsulation.
- **Observer** ties logic loosely together.
- **Adapter** proves you can safely integrate chaotic external code.
- **Facade** hides all the subsystem complexity from the `main()` presentation layer.

---

## Minimal but Convincing Scenario (`main` demo)

```java
Document doc = new DocumentBuilder()
    .setTitle("Design Pattern Demo")
    .addParagraph("This shows 10 patterns.")
    .addImage("diagram.png")
    .build();

// Decorator
Element element = new BoldDecorator(new TextElement("Important"));

// Strategy + Facade
DocumentExporterFacade facade = new DocumentExporterFacade();
facade.export(doc, "export.pdf", new PDFExportStrategy());
facade.export(doc, "export.html", new HTMLExportStrategy());

// Command (undo)
Command addCmd = new AddElementCommand(doc, element);
addCmd.execute();
addCmd.undo();

// Iterator
for (Element e : doc) { // Iterable
    System.out.println(e.render());
}
```
