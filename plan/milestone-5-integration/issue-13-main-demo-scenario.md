# Issue: Main Demo Scenario — End-to-End Integration

## Description
Create the `main()` entry point that demonstrates all 10 design patterns working together in a cohesive scenario. This is the showcase for the teacher demo, illustrating how each pattern solves a real problem.

## Objectives
- Write a `Main.java` that demonstrates every pattern with clear, commented usage
- Showcase pattern interactions (e.g., Builder using Factory, Facade using Strategy and Adapter)
- Produce verifiable output (printed to console and exported files)

## Tasks
- [ ] Write `main()` method with labeled sections for each pattern:

  ### **Singleton** (Creational)
  - `Configuration.getInstance().get("app.name")`
  - `Logger.getInstance().info("Application started")`

  ### **Factory Method** (Creational)
  - Use `ElementFactory.createElement("text", Map.of("content", "Hello"))`
  - Show creation of all three element types

  ### **Builder** (Creational)
  - `Document doc = new DocumentBuilder().setTitle("Demo").addParagraph(...).addImage(...).build()`
  - Show fluent API chaining

  ### **Decorator** (Structural)
  - `Element bold = new BoldDecorator(new TextElement("Important"))`
  - `Element styled = new ItalicDecorator(new ColorDecorator(bold, "red"))`
  - Show composed formatting in `render()` output

  ### **Adapter** (Structural)
  - Show `PDFLibraryAdapter` implementing `Exporter` and wrapping `PDFGenerator`
  - Demonstrate that only `Exporter` interface is used by client code

  ### **Facade** (Structural)
  - `DocumentExporterFacade facade = new DocumentExporterFacade()`
  - `facade.exportHTML(doc, "output.html")`
  - Demonstrate simplified export API

  ### **Strategy** (Behavioral)
  - `facade.export(doc, "out.pdf", new PDFExportStrategy())`
  - `facade.export(doc, "out.md", new MarkdownExportStrategy())`
  - Show runtime strategy interchangeability

  ### **Observer** (Behavioral)
  - Subscribe `ConsoleObserver` and `LoggingObserver` to the document's event bus
  - Make changes to document and verify observers react

  ### **Command** (Behavioral)
  - `CommandHistory history = new CommandHistory()`
  - `history.executeCommand(new AddElementCommand(doc, element))`
  - `history.undo()` — verify element removed
  - `history.redo()` — verify element re-added

  ### **Iterator** (Behavioral)
  - `for (Element e : doc) { System.out.println(e.render()) }`
  - Show filtered iteration: `doc.iterator(ElementTypeFilter.textOnly())`

- [ ] Ensure output is formatted clearly with section headers so the demo is readable
- [ ] All exported files (HTML, PDF, Markdown) are written to an `output/` directory

## Acceptance Criteria
- `Main.main()` compiles and runs without errors
- All 10 patterns are demonstrated with labeled console output
- Generated output files are valid and readable
- The demo clearly shows each pattern's role and value proposition

## Technical Notes
- Add brief `// Pattern: [Name] — [Purpose]` comments before each section for clarity
- The demo is the evaluation artifact — make it clean, readable, and well-organized
- Consider adding a `DemoUtils` class with helper methods for consistent output formatting
