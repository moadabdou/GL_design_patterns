# Issue: Iterator Pattern — Element Traversal

## Description
Implement the Iterator pattern to provide a uniform way to traverse document elements without exposing the underlying data structure. This enhances the `Document` class with a proper iterator that supports filtering and safe traversal.

## Objectives
- Create a custom `DocumentIterator` that implements `Iterator<Element>`
- Support filtered iteration (e.g., only text elements, only image elements)
- Ensure the iterator provides a fail-fast behavior on concurrent modification

## Tasks
- [ ] Create `behavioral/DocumentIterator.java` — implements `Iterator<Element>` with:
  - Constructor takes `Document` and optional `ElementTypeFilter`
  - `hasNext()` / `next()` for traversal
  - `remove()` — delegates to document (optional, can throw `UnsupportedOperationException` for safety)
  - Maintains a cursor position and a filter predicate
- [ ] Create `behavioral/ElementTypeFilter.java` — functional interface:
  - `boolean matches(Element element)`
  - Static factory methods: `textOnly()`, `imageOnly()`, `all()`, `decoratedOnly()`
- [ ] Update `Document.iterator()` to return a `DocumentIterator` (or keep the default, add `iterator(ElementTypeFilter)` overload)
- [ ] Ensure fail-fast behavior: track a modification count in `Document`, check in iterator methods
- [ ] Write unit tests verifying:
  - Iterator traverses all elements in order
  - Filtered iteration returns only matching elements
  - Fail-fast throws `ConcurrentModificationException` if document is modified during iteration
  - Multiple iterators can coexist independently on the same document

## Acceptance Criteria
- `Document` supports `for (Element e : document)` (enhanced for-each loop)
- Filtered iteration is available via `document.iterator(filter)`
- Concurrent modification during iteration is detected and reported
- External code never accesses the internal element list directly

## Pattern Reference
- **Iterator** (Behavioral): Provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation.

## Technical Notes
- The custom iterator wraps the internal list's iterator and adds filtering logic
- Modification count can be an `int` field in `Document` incremented on every mutation
- Cross-pattern: strategies use this iterator to traverse elements for export
- The iterator should copy the modification count at creation time and compare on each `next()` call
