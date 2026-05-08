# Issue: Factory Method Pattern — Element Creation

## Description
Implement the Factory Method pattern to centralize and encapsulate the creation of different `Element` types. Instead of calling constructors directly, clients request elements through a factory, which decouples creation logic from usage.

## Objectives
- Create an `ElementFactory` abstract class / interface
- Implement concrete subclasses or a parameterized factory method
- The factory should produce `TextElement`, `ImageElement`, and `TableElement` based on input parameters

## Tasks
- [ ] Create `creational/ElementFactory.java` with a static or instance method:
  - `Element createElement(String type, Map<String, String> properties)`
  - Types: `"text"`, `"image"`, `"table"`
  - Properties map provides necessary parameters (content, path, rows, etc.)
- [ ] Alternatively, implement the classic Factory Method with subclasses:
  - `TextElementFactory extends ElementFactory`
  - `ImageElementFactory extends ElementFactory`
  - `TableElementFactory extends ElementFactory`
- [ ] Handle invalid type requests gracefully (throw `IllegalArgumentException` with clear message)
- [ ] Write unit tests verifying:
  - Factory produces correct `Element` subclass for each type
  - Factory sets properties correctly on created elements
  - Invalid type throws appropriate exception

## Acceptance Criteria
- Clients never instantiate elements directly (only through the factory)
- Adding a new element type requires only extending the factory — no client code changes (Open/Closed Principle)
- Factory handles `"text"`, `"image"`, `"table"` types

## Pattern Reference
- **Factory Method** (Creational): Defines an interface for creating an object, but lets subclasses alter the type of objects that will be created.

## Technical Notes
- Consider a `Map<String, String>` for properties to keep the factory interface flexible
- Future elements (e.g., `VideoElement`, `CodeElement`) can be added by extending the factory
- The factory can be used inside `DocumentBuilder.addParagraph()` etc. in the next issue
