# Issue: Observer Pattern — Change Notification System

## Description
Implement the Observer pattern to notify interested parties (UI, logger, status bar) when document state changes or when long-running operations (exports) complete. This decouples event producers from consumers.

## Objectives
- Define `DocumentObserver` interface with notification methods
- Create `DocumentEventBus` (or make `Document` itself the subject) for event management
- Implement concrete observers: `LoggingObserver`, `ConsoleObserver`, `ExportStatusObserver`
- Observers react to document changes (element added/removed) and export lifecycle events

## Tasks
- [ ] Create `behavioral/DocumentEvent.java` — enum or class defining event types:
  - `ELEMENT_ADDED`, `ELEMENT_REMOVED`, `ELEMENT_MODIFIED`
  - `EXPORT_STARTED`, `EXPORT_PROGRESS`, `EXPORT_COMPLETED`, `EXPORT_FAILED`
  - Optionally carry data payload (affected element, progress percentage, etc.)
- [ ] Create `behavioral/DocumentObserver.java` — interface with:
  - `void onEvent(DocumentEvent event, Object data)`
- [ ] Create `behavioral/DocumentEventBus.java` (Subject/Observable) with:
  - `void subscribe(DocumentObserver observer)`
  - `void unsubscribe(DocumentObserver observer)`
  - `void notifyObservers(DocumentEvent event, Object data)`
  - Thread-safe observer list management
- [ ] Integrate `DocumentEventBus` into `Document`:
  - `addElement()` triggers `ELEMENT_ADDED`
  - `removeElement()` triggers `ELEMENT_REMOVED`
  - Optionally, commands push notifications through the bus
- [ ] Implement concrete observers:
  - `LoggingObserver` — logs all events via the `Logger` singleton
  - `ConsoleObserver` — prints events to stdout (simulates UI update)
  - `ExportStatusObserver` — tracks export progress and reports completion
- [ ] Write unit tests verifying:
  - Observers receive notifications when document changes
  - Unsubscribed observers no longer receive notifications
  - Multiple observers receive the same event
  - Events carry appropriate data payloads

## Acceptance Criteria
- Observers are notified of all document mutations
- Adding a new observer requires only implementing `DocumentObserver` — no modification to subjects (Open/Closed Principle)
- The observer system works correctly with Command operations (undo/redo also triggers events)
- Export operations notify `EXPORT_STARTED` and `EXPORT_COMPLETED` events

## Pattern Reference
- **Observer** (Behavioral): Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

## Technical Notes
- `Document` should hold a reference to the `DocumentEventBus` (shared across the application or injected)
- Consider making `DocumentEventBus` a Singleton for application-wide event coordination (cross-pattern integration with Singleton)
- For the demo, observers simply print/log — in a full app they would update UI components
- The Facade should trigger export lifecycle events via the event bus
