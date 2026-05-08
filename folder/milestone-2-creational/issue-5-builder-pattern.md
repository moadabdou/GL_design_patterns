# Issue: Builder Pattern — Document Construction

## Description
Implement the Builder pattern to construct `Document` objects step-by-step. This avoids telescoping constructors and provides a fluent API for building documents with mixed element types.

## Objectives
- Create `DocumentBuilder` with chainable methods for constructing a `Document`
- Support building documents with title, paragraphs, images, and tables
- Provide a final `build()` method that returns the constructed `Document`

## Tasks
- [ ] Create `creational/DocumentBuilder.java` with:
  - Internal reference to a `Document` being constructed
  - `setTitle(String title)` — sets the document title
  - `addParagraph(String text)` — creates a `TextElement` (via Factory) and adds it
  - `addImage(String path, int width, int height)` — creates an `ImageElement` and adds it
  - `addTable(List<List<String>> rows)` — creates a `TableElement` and adds it
  - `build()` — returns the constructed `Document` and resets the builder for reuse
- [ ] Use the `ElementFactory` (from Issue #4) internally to create elements
- [ ] Ensure all methods return `DocumentBuilder` for fluent chaining
- [ ] Write unit tests verifying:
  - Fluent API chaining works end-to-end
  - `build()` returns a `Document` with all added elements in order
  - Builder can be reused after `build()` for a new document

## Acceptance Criteria
- `DocumentBuilder` provides a clean fluent API: `new DocumentBuilder().setTitle(...).addParagraph(...).addImage(...).build()`
- No constructor with more than 2 parameters exists for Document
- Builder uses Factory Method internally (pattern integration)

## Pattern Reference
- **Builder** (Creational): Separates the construction of a complex object from its representation so that the same construction process can create different representations.

## Technical Notes
- The builder can optionally support clearing / resetting without creating a new instance
- Consider adding `addFormattedParagraph(String text, Formatting formatting)` as an extension point that integrates with the Decorator pattern later
- Pattern integration: Builder internally calls Factory Method for element creation
