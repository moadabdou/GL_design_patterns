# Issue: Core Domain Model — Document & Element Base Classes

## Description
Define the core domain abstractions: `Document` (the main aggregate), `Element` (base interface for all content pieces), and concrete element types. This foundation is required by Builder, Factory, Decorator, and Iterator patterns.

## Objectives
- Create the `Element` interface with `render()` and `clone()` methods
- Implement `TextElement`, `ImageElement`, `TableElement` as concrete elements
- Create the `Document` class with internal element storage and `Iterable<Element>` support
- Establish the package structure under `domain/`

## Tasks
- [ ] Create `domain/Element.java` — interface declaring:
  - `String render()` — returns the element's string representation
  - `Element clone()` — for prototype-based operations
- [ ] Create `domain/TextElement.java` — holds `String content`
- [ ] Create `domain/ImageElement.java` — holds `String imagePath`, `int width`, `int height`
- [ ] Create `domain/TableElement.java` — holds `List<List<String>> rows`
- [ ] Create `domain/Document.java` with:
  - `List<Element>` backing store
  - Methods: `addElement(Element)`, `removeElement(Element)`, `getElement(int)`, `getElementCount()`
  - `Iterator<Element> iterator()` — returns an iterator over elements
  - `String getTitle()` / `void setTitle(String)`
- [ ] Write unit tests for element construction and Document operations

## Acceptance Criteria
- `Element` interface is the common type for all content
- `Document` supports adding, removing, and querying elements
- `Document` implements `Iterable<Element>` (enabling for-each loops)
- All elements produce meaningful `render()` output

## Technical Notes
- Elements should override `equals()` and `hashCode()` based on content
- `clone()` should perform a deep copy where applicable
- `Document`'s iterator should return an unmodifiable view to preserve encapsulation (this will be enhanced by the Iterator pattern later)
