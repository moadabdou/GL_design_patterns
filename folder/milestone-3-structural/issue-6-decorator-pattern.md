# Issue: Decorator Pattern — Dynamic Element Formatting

## Description
Implement the Decorator pattern to dynamically add formatting (bold, italic, color, underline) to document elements without subclassing. Decorators wrap an `Element` and enhance its `render()` output.

## Objectives
- Create `ElementDecorator` abstract base class implementing `Element`
- Implement concrete decorators: `BoldDecorator`, `ItalicDecorator`, `ColorDecorator`, `UnderlineDecorator`
- Decorators must be composable (stackable)

## Tasks
- [ ] Create `structural/ElementDecorator.java` — abstract class that:
  - Implements `Element`
  - Holds a reference to a wrapped `Element`
  - Delegates `render()` and `clone()` to the wrapped element (subclasses override `render()`)
- [ ] Create `structural/BoldDecorator.java` — wraps `render()` output with `**...**` (or HTML `<b>...</b>`)
- [ ] Create `structural/ItalicDecorator.java` — wraps with `*...*` or `<i>...</i>`
- [ ] Create `structural/ColorDecorator.java` — parameterized with color string; wraps with `<span style="color:...">...</span>`
- [ ] Create `structural/UnderlineDecorator.java` — wraps with `<u>...</u>`
- [ ] Write unit tests verifying:
  - Single decorator modifies `render()` output correctly
  - Multiple decorators compose (e.g., `new BoldDecorator(new ItalicDecorator(element))`) and produce combined formatting
  - `clone()` works correctly through decorator chains
  - A decorated element can be added to a `Document` and rendered properly

## Acceptance Criteria
- Decorators can be stacked in any order
- Adding a new formatting style requires only a new decorator class — no existing code changes (Open/Closed Principle)
- Decorated elements work seamlessly with `Document`, Builder, and Iterator

## Pattern Reference
- **Decorator** (Structural): Attaches additional responsibilities to an object dynamically. Provides a flexible alternative to subclassing for extending functionality.

## Technical Notes
- Decorators should render in nested format: `BoldDecorator(ItalicDecorator(text))` produces `**<i>text</i>**`
- The `ElementFormatter` utility could parse format specifiers, but decorators are preferred for the pattern demo
- Integration: the Builder's `addFormattedParagraph()` can accept decorator parameters
